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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hula.web.WebConstants;
import com.hula.web.service.ServiceModule;
import com.hula.web.velocity.VelocityInitialiser;

/**
 * Responsible for initialising the HulaWeb application.
 */
public class HulaWebContextListener implements ServletContextListener
{
	private static Logger logger = LoggerFactory.getLogger(HulaWebContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent e)
	{
		try
		{
			Context ctx = new InitialContext();

			// read properties from web.xml
			String httpPort = (String) ctx.lookup("java:comp/env/httpPort");
			String httpsPort = (String) ctx.lookup("java:comp/env/httpsPort");
			String scriptPath = (String) ctx.lookup("java:comp/env/scriptPath");
			String commandPath = (String) ctx.lookup("java:comp/env/commandPath");
			String commandKeys = (String) ctx.lookup("java:comp/env/commandKeys");
			String pagePath = (String) ctx.lookup("java:comp/env/pagePath");
			
			// store on servlet context
			ServletContext sctx = e.getServletContext();
			sctx.setAttribute(WebConstants.httpPort, httpPort);
			sctx.setAttribute(WebConstants.httpsPort, httpsPort);

			Injector injector = Guice.createInjector(new ServiceModule(scriptPath, commandPath, commandKeys));
			sctx.setAttribute(WebConstants.Injector, injector);

			// initialise velocity
			VelocityInitialiser.initialise(pagePath);
		}
		catch (Throwable t)
		{
			logger.error("error initialising HulaWeb", t);
			throw new RuntimeException(t);
		}

		logger.info("HulaWeb started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent e)
	{
	}

}
