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
package com.hula.web.filter;

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

public class HulaWebFilter implements Filter
{
	private static Logger logger = LoggerFactory.getLogger(HulaWebFilter.class);

	@Override
	public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain fc) throws IOException, ServletException
	{
		try
		{
			doFilter(baseRequest, baseResponse);
		}
		catch (Throwable t)
		{
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

		logger.info("request [{}]", request.getRequestURL());

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
			throw new RuntimeException("error parsing script", e);
		}

		// check if we need a channel switch
		String redirectURL = getRedirectURL(script, request);
		if (redirectURL != null)
		{
			// if channel switch will lose content, raise a warning
			if (!request.getParameterMap().isEmpty() && redirectURL.indexOf('?') == -1)
			{
				logger.warn("A change of channel was required, however the parameters on the target URL will be lost");
			}

			response.sendRedirect(redirectURL);
			return;
		}

		// forward to the servlet
		RequestDispatcher rd = request.getRequestDispatcher("/hula?script=" + scriptName);
		rd.forward(baseRequest, baseResponse);

		return;
	}

	protected String getRedirectURL(Script script, HttpServletRequest request)
	{
		String url = request.getRequestURL().toString().toLowerCase();

		boolean usingSecureChannel = url.startsWith("https://");

		// if secure status matches, no channel switch is required
		if (script.isSecure() == usingSecureChannel)
		{
			return null;
		}

		ServletContext sctx = request.getServletContext();
		String httpPort = (String) sctx.getAttribute(WebConstants.httpPort);
		String httpsPort = (String) sctx.getAttribute(WebConstants.httpsPort);

		if (script.isSecure())
		{
			url = url.replace("http://", "https://");
			if (!httpPort.equals("80"))
			{
				url = url.replace(":" + httpPort + "/", ":" + httpsPort + "/");
			}
		}
		else
		{
			url = url.replace("https://", "http://");
			if (!httpPort.equals("80"))
			{
				url = url.replace(":" + httpsPort + "/", ":" + httpPort + "/");
			}
		}
		if (request.getQueryString() != null)
		{
			url += "?" + request.getQueryString();
		}
		return url;
	}


	@Override
	public void init(FilterConfig fc) throws ServletException
	{
	}

	@Override
	public void destroy()
	{
	}
}
