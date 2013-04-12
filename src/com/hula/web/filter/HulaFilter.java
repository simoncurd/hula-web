package com.hula.web.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.web.WebConstants;
import com.hula.web.model.Script;
import com.hula.web.service.script.ScriptService;
import com.hula.web.service.script.ScriptServiceImpl;
import com.hula.web.service.script.exception.ScriptNotFoundException;
import com.hula.web.service.script.exception.ScriptParseException;

public class HulaFilter implements Filter
{
	private static Logger logger = LoggerFactory.getLogger(HulaFilter.class);
//
//	public static String httpPort = null;
//	public static String httpsPort = null;

	@Override
	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain fc) throws IOException, ServletException
	{
		try
		{
			doFilter(baseRequest, baseResponse);
		}
		catch (Throwable t)
		{
			logger.error("error running filter", t);
			throw new RuntimeException(t);
		}

	}

	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) baseRequest;
		HttpServletResponse response = (HttpServletResponse) baseResponse;

		// /hula
		String contextPath = request.getContextPath();
		
		// http://localhost:8080/hula/welcome
		String requestURL = request.getRequestURL().toString();
		int pos = requestURL.indexOf(contextPath) + contextPath.length() + 1;
		
		// welcome
		String scriptName = requestURL.substring(pos, requestURL.length());

		logger.info("");
		logger.info("request [{}]", request.getRequestURL());

		logger.info("context path [{}]", contextPath);
		logger.info("script [{}]", scriptName);

		// load the script
		ScriptService scriptService = ScriptServiceImpl.getInstance();
		Script script = null;
		try
		{
			script = scriptService.getScript(scriptName);
		}
		catch (ScriptNotFoundException e)
		{
			logger.error("error loading script", e);
			response.sendError(404);
			return;
		}
		catch (ScriptParseException e)
		{
			//logger.error("error parsing script", e);
			throw new RuntimeException("error parsing script", e);
		}

		// check if we need a channel switch
		try
		{
			checkRedirect(script, request);
		}
		catch (RedirectRequired e)
		{
			// if they're going to lose content, fail on this
			if (!request.getParameterMap().isEmpty() && e.redirectURL.indexOf('?') == -1)
			{
				throw new RuntimeException("A change of channel was attempted (http/https) was requested, however the parameters on the target URL will be lost");
			}

			response.sendRedirect(e.redirectURL);
			return;
		}

		// forward to the servlet
		logger.info("Run Script [{}]", scriptName);
		RequestDispatcher rd = request.getRequestDispatcher("/hula?script=" + scriptName);
		rd.forward(baseRequest, baseResponse);

		return;
	}
	


	protected void checkRedirect(Script script, HttpServletRequest request) throws RedirectRequired
	{
		String url = request.getRequestURL().toString().toLowerCase();
		
		ServletContext sctx = request.getServletContext();
		String httpPort = (String)sctx.getAttribute(WebConstants.httpPort);
		String httpsPort = (String)sctx.getAttribute(WebConstants.httpsPort);

		String queryParameters = "";
		if (request.getQueryString() != null)
		{
			queryParameters = "?" + request.getQueryString();
		}

		boolean usingSecureChannel = url.startsWith("https://");

		if (script.isSecure() && !usingSecureChannel)
		{
			url = url.replace("http://", "https://");
			if (!httpPort.equals("80"))
			{
				url = url.replace(":" + httpPort + "/", ":" + httpsPort + "/");
			}
			url += queryParameters;

			throw new RedirectRequired(url);
		}
		else if (!script.isSecure() && usingSecureChannel)
		{
			url = url.replace("https://", "http://");
			if (!httpPort.equals("80"))
			{
				url = url.replace(":" + httpsPort + "/", ":" + httpPort + "/");
			}
			url += queryParameters;

			throw new RedirectRequired(url);
		}
	}

	class RedirectRequired extends Exception
	{
		private String redirectURL;

		RedirectRequired(String redirectURL)
		{
			this.redirectURL = redirectURL;
		}
	}


	//
	// Filter Lifecycle methods
	//

	@Override
	public void init(FilterConfig fc) throws ServletException
	{
		logger.info("Filter Setup");
		

		logger.info("Filter Setup - complete");
	}

	@Override
	public void destroy()
	{
		logger.info("Filter Destroy");
	}
}
