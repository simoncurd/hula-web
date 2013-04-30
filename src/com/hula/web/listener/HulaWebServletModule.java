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
package com.hula.web.listener;

import java.util.Properties;

import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.hula.lang.util.FileUtil;
import com.hula.web.filter.HulaWebFilter;
import com.hula.web.model.runresult.RedirectRunResult;
import com.hula.web.model.runresult.ReturnContentRunResult;
import com.hula.web.model.runresult.ShowPageRunResult;
import com.hula.web.response.RedirectProcessor;
import com.hula.web.response.ResponseProcessor;
import com.hula.web.response.ReturnContentProcessor;
import com.hula.web.response.ShowPageProcessor;
import com.hula.web.service.script.ScriptService;
import com.hula.web.service.script.ScriptServiceImpl;
import com.hula.web.service.session.SessionService;
import com.hula.web.service.session.SessionServiceImpl;
import com.hula.web.servlet.HulaWebServlet;
import com.hula.web.velocity.VelocityInitialiser;

/**
 * Configures services, filters, servlets required by the Hula Web application. 
 */
public class HulaWebServletModule extends ServletModule
{
	private Properties configProperties;

	public HulaWebServletModule()
	{
		super();

		// load the configuration properties
		this.configProperties = FileUtil.loadProperties("config.properties");

		// initialise Velocity
		VelocityInitialiser.initialise(configProperties.getProperty("page.path"));
	}

	@Override
	protected void configureServlets()
	{
		// bind the properties
		Names.bindProperties(binder(), this.configProperties);

		// setup services
		bind(SessionService.class).to(SessionServiceImpl.class);
		bind(ScriptService.class).to(ScriptServiceImpl.class);

		// response processors
		MapBinder<String, ResponseProcessor> responseProcessorMap = MapBinder.newMapBinder(binder(), String.class, ResponseProcessor.class);
		responseProcessorMap.addBinding(ShowPageRunResult.class.getSimpleName()).to(ShowPageProcessor.class);
		responseProcessorMap.addBinding(ReturnContentRunResult.class.getSimpleName()).to(ReturnContentProcessor.class);
		responseProcessorMap.addBinding(RedirectRunResult.class.getSimpleName()).to(RedirectProcessor.class);

		// filter
		filter("/*").through(HulaWebFilter.class);

		// servlet
		serve("/exec*").with(HulaWebServlet.class);

	}

}
