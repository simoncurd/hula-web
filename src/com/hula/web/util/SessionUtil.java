package com.hula.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUtil
{
	private static String SESSION_ID = "PVSESSIONID";
	
	public static boolean hasSessionIdentifier(HttpServletRequest req)
	{
		if (req.getCookies() == null)
		{
			return false;
		}
		for (Cookie cookie : req.getCookies())
		{
			if (cookie.getName().equals(SESSION_ID))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String getSessionIdentifier(HttpServletRequest req)
	{
		if (req.getCookies() == null)
		{
			return null;
		}
		for (Cookie cookie : req.getCookies())
		{
			if (cookie.getName().equals(SESSION_ID))
			{
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public static void setSessionIdentifier(HttpServletResponse res, String id)
	{
		Cookie cookie = new Cookie(SESSION_ID, id);
		res.addCookie(cookie);
	}
	
	public static void removeSessionIdentifier(HttpServletResponse res)
	{
		Cookie sessionCookie = new Cookie(SESSION_ID, null);
		sessionCookie.setMaxAge(0);
		sessionCookie.setPath("/");
		res.addCookie(sessionCookie);
	}
}
