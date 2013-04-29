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
package com.hula.web.response;

import java.io.IOException;
import java.io.PrintWriter;

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

import com.hula.web.model.HulaWebContext;
import com.hula.web.model.runresult.ShowPageRunResult;

/**
 * Renders a Velocity-based page. <br/>
 * 
 * See also {@link com.hula.web.model.runresult.ShowPageRunResult}, {@link com.hula.web.commands.results.SetView}
 */
public class ShowPageProcessor implements ResponseProcessor
{
	private static Logger logger = LoggerFactory.getLogger(ShowPageProcessor.class);

	@Override
	public void process(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response) throws IOException
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

		// setup the page
		response.setContentType("text/html");

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

}
