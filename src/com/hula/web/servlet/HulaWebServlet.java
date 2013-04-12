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
package com.hula.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.lang.runtime.HulaContext;
import com.hula.lang.runtime.HulaPlayer;
import com.hula.lang.runtime.HulaPlayerImpl;
import com.hula.web.WebConstants;
import com.hula.web.model.RunResult;
import com.hula.web.model.HulaWebContext;
import com.hula.web.model.runresult.ForwardRunResult;
import com.hula.web.model.runresult.RedirectRunResult;
import com.hula.web.model.runresult.ReturnContentRunResult;
import com.hula.web.model.runresult.ShowPageRunResult;
import com.hula.web.service.script.ScriptService;
import com.hula.web.service.script.ScriptServiceImpl;
import com.hula.web.util.ParameterUtil;
import com.hula.web.util.SessionUtil;
import com.hula.web.util.URLUtils;

public class HulaWebServlet extends HttpServlet
{
	private static Logger logger = LoggerFactory.getLogger(HulaWebServlet.class);
	private ScriptService scriptService = null;

	public void init(ServletConfig config) throws ServletException
	{
		scriptService = ScriptServiceImpl.getInstance();
		super.init(config);
	}

	protected String getRequestPath(HttpServletRequest request)
	{
		String scriptName = request.getParameter("script");
		String url = "/" + scriptName;
		return url;
	}

	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		logger.info("request URL [" + request.getRequestURL() + "]");
		String requestURL = request.getRequestURL().toString();

		// setup the page
		response.setContentType("text/html");

		// where a parameter is on the request...
		String scriptName = request.getParameter("script");

		if (scriptName == null || scriptName.trim().equals(""))
		{
			scriptName = "default";
		}

		logger.info("Execute Script [" + scriptName + "]");

		HulaWebContext hctx = new HulaWebContext();
		buildHulaContext(request, response, hctx);

		String urlString = getRequestPath(request);

		hctx.setParameter(WebConstants.URI, urlString);
		hctx.setParameter(WebConstants.ScriptName, scriptName);

		// lookup the session identifier from the cookies
		if (SessionUtil.hasSessionIdentifier(request))
		{
			hctx.setSessionId(SessionUtil.getSessionIdentifier(request));
		}

		// build a list of scripts to execute
		List<String> scripts = new ArrayList<String>();

		if (scriptService.hasScript("common"))
		{
			scripts.add("common");
		}
		scripts.add(scriptName);

		for (String scriptItem : scripts)
		{
			PostRunAction action = runScript(scriptItem, hctx, request, response);
			logger.info("script [" + scriptItem + "] finished");
			if (action.equals(PostRunAction.Quit))
			{
				break;
			}
		}

	}

	enum PostRunAction
	{
		Quit, Continue;
	}

	protected PostRunAction runScript(String scriptName, HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			HulaPlayer player = new HulaPlayerImpl(scriptService);
			player.run(scriptName, hctx);
		}
		catch (Throwable e)
		{
			logger.error("error running script", e);
			throw new RuntimeException(e);
		}

		// need to save sessionId if one has been created
		saveSession(hctx, request, response);

		if (hctx.getRunResult() != null)
		{
			RunResult runResult = hctx.getRunResult();

			if (runResult instanceof RedirectRunResult)
			{
				// process redirect
				RedirectRunResult res = (RedirectRunResult) hctx.getRunResult();
				processRedirect(hctx, request, response, res.getAction());
				return PostRunAction.Quit;
			}
			else if (runResult instanceof ShowPageRunResult)
			{
				renderView(hctx, request, response);
				return PostRunAction.Quit;
			}
			else if (runResult instanceof ReturnContentRunResult)
			{
				returnContent(hctx, request, response);
				return PostRunAction.Quit;
			}
			else if (runResult instanceof ForwardRunResult)
			{
				ForwardRunResult fwd = (ForwardRunResult) hctx.getRunResult();
				return this.runScript(fwd.getScriptName(), hctx, request, response);
			}
		}

		return PostRunAction.Continue;

	}

	protected void returnContent(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// result page holds the id of the content on the cctx to return

		ReturnContentRunResult result = (ReturnContentRunResult) hctx.getRunResult();
		String resultId = result.getContentId();
		logger.info("Return contents of var [" + resultId + "]");

		Object o = hctx.getParameter(resultId);
		logger.info("Return content type [" + o.getClass().getSimpleName() + "]");

		if (o instanceof String)
		{
			logger.info("Return content [" + o.toString() + "]");
			PrintWriter out = response.getWriter();
			out.write(o.toString());
		}

	}

	protected void renderView(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		ShowPageRunResult result = (ShowPageRunResult) hctx.getRunResult();

		String resultPage = result.getPageName();
		if (resultPage == null)
		{
			resultPage = "default";
		}

		// setup Velocity context
		VelocityContext vctx = new VelocityContext();

		vctx.put("datetool", new DateTool());
		vctx.put("numbertool", new NumberTool());

		for (String id : hctx.getParameters().keySet())
		{
			Object value = hctx.getParameters().get(id);
			logger.info("--- Copying [" + id + "] value [" + value + "] to context");
			vctx.put(id, value);
		}

		// put the session onto the velocity context
		vctx.put("session", request.getSession());

		// process Velocity template
		PrintWriter out = response.getWriter();
		logger.info("render [" + resultPage + "]");

		String templateName = resultPage + ".vm";

		try
		{
			Template template = Velocity.getTemplate(templateName);
			template.merge(vctx, out);
		}
		catch (ResourceNotFoundException t)
		{
			throw new RuntimeException("Error finding page [" + templateName + "]", t);
		}
		catch (ParseErrorException t)
		{
			throw new RuntimeException("Error parsing page [" + templateName + "]", t);
		}

	}

	/**
	 * need to save sessionId if one has been created
	 * 
	 * @param cctx
	 */
	protected void saveSession(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response)
	{
		if (hctx.getSessionId() != null)
		{
			SessionUtil.setSessionIdentifier(response, hctx.getSessionId());
		}
		else
		{
			// the sessionId has been removed from the context, so lets remove it
			// from the browser
			if (SessionUtil.hasSessionIdentifier(request))
			{
				SessionUtil.removeSessionIdentifier(response);
			}
		}
	}

	private void processRedirect(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response, String script) throws IOException
	{
		RedirectRunResult redirectResult = (RedirectRunResult) hctx.getRunResult();
		String url = URLUtils.getRedirectURL(redirectResult, request.getRequestURL().toString(), request.getServerName(), request.getServerPort());
		response.sendRedirect(url);
	}

	private void buildHulaContext(HttpServletRequest request, HttpServletResponse response, HulaContext hctx)
	{
		Map<String, String[]> params = request.getParameterMap();

		Map<String, String> convertedParams = ParameterUtil.convertParameterMap(params);
		for (String id : convertedParams.keySet())
		{
			String value = convertedParams.get(id);
			logger.info("--- Copying [" + id + "] value [" + value + "]");
			hctx.setParameter(id, value);
		}

		// hctx.setRuntimeAdapter(new WebRequestEnvironmentAdapterImpl(request, response));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		handleRequest(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		handleRequest(request, response);
	}
}
