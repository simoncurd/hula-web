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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.web.model.HulaWebContext;
import com.hula.web.model.runresult.ReturnContentRunResult;

/**
 * Returns a content-based response based on a variable value. <br/>
 * 
 * See also {@link com.hula.web.model.runresult.ReturnContentRunResult}, {@link com.hula.web.commands.results.ReturnContent}
 */
public class ReturnContentProcessor implements ResponseProcessor
{
	private static Logger logger = LoggerFactory.getLogger(ReturnContentProcessor.class);

	@Override
	public void process(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		ReturnContentRunResult result = (ReturnContentRunResult) hctx.getRunResult();
		Object content = result.getContent();
		logger.info("Return content type [" + content.getClass().getSimpleName() + "]");

		if (content instanceof String)
		{
			logger.info("Return content [" + content.toString() + "]");
			PrintWriter out = response.getWriter();
			out.write(content.toString());
		}
	}

}
