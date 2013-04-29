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
package com.hula.web.util;

import gumi.builders.UrlBuilder;

import java.util.Map;

import com.hula.web.model.runresult.RedirectRunResult;

/**
 * Utility class with methods for working with URLs
 */
public class URLUtils
{
	/**
	 * Build an absolute URL for redirection
	 * 
	 * @param redirectRunResult The {@link RedirectRunResult} containing redirect requirements
	 * @param requestURL The current request URL
	 * @return the redirect URL
	 */
	public static String getRedirectURL(RedirectRunResult redirectRunResult, String requestURL, String context)
	{
		String target = redirectRunResult.getTarget();

		UrlBuilder url = UrlBuilder.fromString(requestURL);

		if (target.startsWith("http"))
		{
			// target script is a fully-qualified URL
			url = UrlBuilder.fromString(target);
		}
		else
		{
			// target script is an action
			url = url.withPath(context + "/" + target);
		}

		// append any new parameters
		Map<String, String> parameters = redirectRunResult.getParameters();
		if (parameters != null)
		{
			for (String key : parameters.keySet())
			{
				String value = parameters.get(key);
				url = url.addParameter(key, value);
			}
		}

		return url.toString();
	}

	/**
	 * Extract the script name from a URL
	 * 
	 * @param requestURL The URL to inspect
	 * @param contextPath The context path of the current web application
	 * @return the name of the script
	 */
	public static String getScriptName(String requestURL, String contextPath)
	{
		UrlBuilder ub = UrlBuilder.fromString(requestURL);

		String scriptName = ub.path;

		// strip off the context
		if (contextPath != null && !contextPath.equals(""))
		{
			scriptName = scriptName.substring(contextPath.length());
		}

		// strip off the leading forward-slash
		return scriptName.substring(1);
	}

}
