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
package com.hula.web.service.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class with methods for working with sessions
 */
public class SessionUtil
{
	private static String SESSION_ID = "HLSESSIONID";

	/**
	 * Check the request for a session identifier
	 * 
	 * @param request The requst
	 * @return boolean indicating if a session identifier was found
	 */
	public static boolean hasSessionIdentifier(HttpServletRequest request)
	{
		if (request.getCookies() == null)
		{
			return false;
		}
		for (Cookie cookie : request.getCookies())
		{
			if (cookie.getName().equals(SESSION_ID))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the session identifier from the request
	 * 
	 * @param request The request
	 * @return Session identifier, or null
	 */
	public static String getSessionIdentifier(HttpServletRequest request)
	{
		if (request.getCookies() != null)
		{
			for (Cookie cookie : request.getCookies())
			{
				if (cookie.getName().equals(SESSION_ID))
				{
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Set a session identifier on the response
	 * 
	 * @param response The response
	 * @param id The session identifier
	 */
	public static void setSessionIdentifier(HttpServletResponse response, String id)
	{
		Cookie cookie = new Cookie(SESSION_ID, id);
		response.addCookie(cookie);
	}

	/**
	 * Remove the session identifier on the response
	 * 
	 * @param response The response
	 */
	public static void removeSessionIdentifier(HttpServletResponse response)
	{
		Cookie sessionCookie = new Cookie(SESSION_ID, null);
		sessionCookie.setMaxAge(0);
		sessionCookie.setPath("/");
		response.addCookie(sessionCookie);
	}
}
