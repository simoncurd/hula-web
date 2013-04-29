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
package com.hula.web.service;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.hula.web.model.runresult.RedirectRunResult;
import com.hula.web.model.runresult.ReturnContentRunResult;
import com.hula.web.model.runresult.ShowPageRunResult;
import com.hula.web.response.RedirectProcessor;
import com.hula.web.response.ResponseProcessor;
import com.hula.web.response.ReturnContentProcessor;
import com.hula.web.response.ShowPageProcessor;
import com.hula.web.runtime.ResponseProcessorMapping;
import com.hula.web.service.script.ScriptService;
import com.hula.web.service.script.ScriptServiceImpl;
import com.hula.web.service.session.SessionService;
import com.hula.web.service.session.SessionServiceImpl;

public class ServiceModule extends AbstractModule
{
	private String scriptPath;
	private String commandPath;
	private String commandKeys;

	public ServiceModule(String scriptPath, String commandPath, String commandKeys)
	{
		super();
		this.scriptPath = scriptPath;
		this.commandPath = commandPath;
		this.commandKeys = commandKeys;
	}

	@Override
	protected void configure()
	{
		bind(String.class).annotatedWith(Names.named("script.path")).toInstance(scriptPath);
		bind(String.class).annotatedWith(Names.named("command.path")).toInstance(commandPath);
		bind(String.class).annotatedWith(Names.named("command.keys")).toInstance(commandKeys);

		bind(SessionService.class).to(SessionServiceImpl.class);
		bind(ScriptService.class).to(ScriptServiceImpl.class);

		MapBinder<String, ResponseProcessor> bi = MapBinder.newMapBinder(binder(), String.class, ResponseProcessor.class);
		bi.addBinding(ShowPageRunResult.class.getSimpleName()).to(ShowPageProcessor.class);
		bi.addBinding(ReturnContentRunResult.class.getSimpleName()).to(ReturnContentProcessor.class);
		bi.addBinding(RedirectRunResult.class.getSimpleName()).to(RedirectProcessor.class);
		
		bind(ResponseProcessorMapping.class);
	}

}
