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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
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

/**
 * Service for loading and parsing Hula scripts from disk
 */
@Singleton
public class ScriptServiceImpl implements ScriptService
{
	private CommandFactory commandFactory = null;
	private Properties secureScripts = null;
	private String scriptPath = null;

	/**
	 * Construct the ScriptService.
	 * 
	 * @param scriptPath The path to the scripts
	 * @param commandPath The path to the commands
	 * @param commandKeys A comma-separated list of filename prefixes for command files (*.commands.properties)
	 */
	@Inject
	public ScriptServiceImpl(@Named("script.path") String scriptPath, @Named("command.path") String commandPath, @Named("command.keys") String commandKeys)
	{
		this.scriptPath = scriptPath;

		// setup the command factory
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

		// setup the properties file for secure scripts
		this.secureScripts = new Properties();
		InputStream in = FileUtil.getFileInputStream(scriptPath + "/secure.properties");
		try
		{
			secureScripts.load(in);
		}
		catch (IOException e)
		{
			throw new RuntimeException("error loading secure properties", e);
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				// swallow - nothing we can do about this
			}
		}

	}

	@Override
	public boolean hasScript(String name)
	{
		InputStream inputStream = FileUtil.getFileInputStream(getScriptPath(name));

		boolean result = (inputStream != null) ? true : false;
		try
		{
			inputStream.close();
		}
		catch (IOException e)
		{
			// swallow - nothing we can do about this
		}
		return result;
	}

	@Override
	public HulaExecutable read(String name) throws IOException, HulaParserException
	{
		InputStream inputStream = FileUtil.getFileInputStream(getScriptPath(name));
		StringBuilder scriptContainer = new StringBuilder();

		try
		{
			// load the file
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = in.readLine()) != null)
			{
				scriptContainer.append(line);
				scriptContainer.append("\n");
			}
			in.close();
		}
		finally
		{
			inputStream.close();
		}

		// parse script
		HulaParser parser = new HulaParserImpl(commandFactory);
		HulaExecutable exec = parser.parse(scriptContainer.toString());
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

	/**
	 * Checks if the named script is secure
	 * 
	 * @param name The script to check
	 * @return flag indicating if the script is secure
	 * @throws IOException
	 */
	private boolean isSecure(String name) throws IOException
	{
		return this.secureScripts.containsKey(name + ".txt");
	}

	/**
	 * Get the relative path to the script
	 * 
	 * @param name The name of the script
	 * @return The relative path to the script
	 */
	private String getScriptPath(String name)
	{
		return scriptPath + "/" + name + ".txt";
	}

}
