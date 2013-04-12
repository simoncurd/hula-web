package com.hula.web.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.web.WebConstants;
import com.hula.web.filter.HulaFilter;
import com.hula.web.service.script.ScriptServiceImpl;
import com.hula.web.velocity.VelocityInitialiser;

public class HulaContextListener implements ServletContextListener
{
	private static Logger logger = LoggerFactory.getLogger(HulaContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent e)
	{
		try
		{
			Context ctx = new InitialContext();
			
			ServletContext sctx = e.getServletContext();
			

			// FIXME: find a better way of doing this
			//HulaFilter.httpPort = (String) ctx.lookup("java:comp/env/httpPort");
			//HulaFilter.httpsPort = (String) ctx.lookup("java:comp/env/httpsPort");

			String httpPort = (String) ctx.lookup("java:comp/env/httpPort");
			String httpsPort = (String) ctx.lookup("java:comp/env/httpsPort");
			sctx.setAttribute(WebConstants.httpPort, httpPort);
			sctx.setAttribute(WebConstants.httpsPort, httpsPort);
			
			String scriptPath = (String) ctx.lookup("java:comp/env/scriptPath");
			String commandPath = (String) ctx.lookup("java:comp/env/commandPath");
			String commandKeys = (String) ctx.lookup("java:comp/env/commandKeys");
			String velocityPath = (String) ctx.lookup("java:comp/env/velocityPath");

			// initialise the script service
			ScriptServiceImpl.initialise(scriptPath, commandPath, commandKeys);

			// initialise velocity
			VelocityInitialiser.initialise(velocityPath);
		}
		catch (Throwable t)
		{
			logger.error("error initialising HulaContextListener", t);
			throw new RuntimeException(t);
		}

		logger.info("WebController started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent e)
	{
	}

}
