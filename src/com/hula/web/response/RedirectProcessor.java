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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hula.web.model.HulaWebContext;
import com.hula.web.model.runresult.RedirectRunResult;
import com.hula.web.util.URLUtils;

/**
 * Redirects the request to an alternative script or URL. <br/>
 * 
 * See Also {@link com.hula.web.model.runresult.RedirectRunResult}, {@link com.hula.web.commands.results.Redirect}
 */
public class RedirectProcessor implements ResponseProcessor
{

	@Override
	public void process(HulaWebContext hctx, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		RedirectRunResult redirectResult = (RedirectRunResult) hctx.getRunResult();
		String url = URLUtils.getRedirectURL(redirectResult, request.getRequestURL().toString(), request.getContextPath());
		response.sendRedirect(url);
	}

}
