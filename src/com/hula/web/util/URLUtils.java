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

import java.util.Map;

import com.hula.web.model.runresult.RedirectRunResult;

public class URLUtils
{
	/**
	 * Build an absolute URL for redirection
	 *  
	 * @param redirectRunResult The {@link RedirectRunResult} 
	 * @param requestURL The current request URL
	 * @param serverName The server name
	 * @param serverPort The server port
	 * @return the redirect URL
	 */
	public static String getRedirectURL(RedirectRunResult redirectRunResult, String requestURL, String serverName, int serverPort)
	{
		String target = redirectRunResult.getAction();

		String url = null;

		// target script is a fully-qualified URL
		if (target.startsWith("http"))
		{
			url = target;
		}

		// target script is an action
		else
		{

			String path = "/" + target;

			// grab the protocol
			url = requestURL.substring(0, requestURL.indexOf(':')) + "://";

			// append the hostname
			url += serverName;

			// append the port (if necessary)
			if (serverPort != 80 && serverPort != 443)
			{
				url += ":" + serverPort;
			}
			// append the path
			url += path;

		}

		// append any new parameters
		String queryString = createQueryString(redirectRunResult.getParameters());
		if (queryString != null && !queryString.equals(""))
		{

			if (url.indexOf("?") == -1)
			{
				url += "?";
			}
			else
			{
				url += "&";
			}

			url += queryString;
		}


		return url;
	}
	

	public static String createQueryString(Map<String,String[]> parameters)
	{
		String result = "";
		for (String key : parameters.keySet())
		{
			String[] values = parameters.get(key);
			String value = values[0];
			if (values.length > 1)
			{
				for (int index =1 ; index != values.length; index++)
				{
					value += "," + values[index];
				}
			}
			if (result.length() > 0)
			{
				result += "&";
			}
			result += key + "=" + value;
		}
		return result;
	}
}
