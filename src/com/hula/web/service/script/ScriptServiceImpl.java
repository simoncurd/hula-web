/**
 * Copyright 2013 Simon Curd <simoncurd@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hula.web.service.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.lang.factory.CommandFactory;
import com.hula.lang.factory.CommandFactoryImpl;
import com.hula.lang.parser.HulaExecutable;
import com.hula.lang.parser.HulaParser;
import com.hula.lang.parser.HulaParserImpl;
import com.hula.lang.parser.exception.HulaParserException;
import com.hula.lang.util.FileUtil;
import com.hula.web.model.Script;
import com.hula.web.service.script.exception.ScriptNotFoundException;
import com.hula.web.service.script.exception.ScriptParseException;

public class ScriptServiceImpl implements ScriptService
{
	private static Logger logger = LoggerFactory.getLogger(ScriptServiceImpl.class);

	private static ScriptServiceImpl impl = null;
	private CommandFactory commandFactory = null;
	private String scriptPath = null;

	public static ScriptServiceImpl initialise(String scriptPath, String commandPath, String commandKeys)
	{
		if (impl == null)
		{
			impl = new ScriptServiceImpl(scriptPath, commandPath, commandKeys);
		}
		return impl;
	}

	public static ScriptServiceImpl getInstance()
	{
		return impl;
	}

	private ScriptServiceImpl(String scriptPath, String commandPath, String commandKeys)
	{
		this.scriptPath = scriptPath;

		this.commandFactory = new CommandFactoryImpl();
		try
		{
			String[] keys = commandKeys.split(",");
			for (String key : keys)
			{
				commandFactory.loadCommands(commandPath + key + ".commands.properties");
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("error loading hula commands", e);
		}

	}

	@Override
	public boolean hasScript(String name)
	{
		File scriptFile = new File(getScriptPath(name));
		return scriptFile.exists();
	}

	@Override
	public HulaExecutable read(String name) throws IOException, HulaParserException
	{
		InputStream inputStream = FileUtil.getFileInputStream(getScriptPath(name));

		// load the file
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = in.readLine()) != null)
		{
			sb.append(line);
			sb.append("\n");
		}
		in.close();

		// FIXME: do we need this?
		inputStream.close();

		// parse script
		HulaParser parser = new HulaParserImpl(commandFactory);
		HulaExecutable exec = parser.parse(sb.toString());
		return exec;
	}

	@Override
	public Script getScript(String name) throws ScriptNotFoundException, ScriptParseException
	{
		try
		{
			// load the script
			Script script = new Script();
			script.setName(name);

			HulaExecutable exec = read(name);

			script.setSecure(isSecure(name));
			script.setContent(exec.getContent());

			return script;
		}
		catch (IOException e)
		{
			throw new ScriptNotFoundException(e);
		}
		catch (HulaParserException e)
		{
			throw new ScriptParseException(e);
		}
	}

	private boolean isSecure(String name) throws IOException
	{
		// FIXME: loads secure.properties every time
		Properties secureProperties = new Properties();

		String path = scriptPath + "/secure.properties";
		secureProperties.load(FileUtil.getFileInputStream(path));

		return secureProperties.containsKey(name + ".txt");

	}

	private String getScriptPath(String name)
	{
		return scriptPath + "/" + name + ".txt";
	}

}
