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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Singleton;
import com.hula.web.model.HulaWebContext;
import com.hula.web.model.SessionContainer;

/**
 * Basic implementation of SessionService, providing access to the HttpSession
 */
@Singleton
public class SessionServiceImpl implements SessionService
{

	public SessionServiceImpl()
	{
	}

	@Override
	public SessionContainer getSession(boolean create, HulaWebContext hctx)
	{
		HttpServletRequest request = hctx.getRequest();

		// if we've already initialised the session, return it
		if (hctx.getSessionContainer() != null)
		{
			return hctx.getSessionContainer();
		}

		// find, and potentially create
		HttpSession session = request.getSession(create);
		if (session == null)
		{
			return null;
		}

		SessionContainer sessionContainer = new SessionContainer();

		// copy session attributes to session container
		Enumeration<String> sessionAttributes = session.getAttributeNames();
		while (sessionAttributes.hasMoreElements())
		{
			String name = sessionAttributes.nextElement();
			Object value = session.getAttribute(name);
			sessionContainer.put(name, value.toString());
		}
		hctx.setSessionContainer(sessionContainer);
		return sessionContainer;
	}

	@Override
	public void saveSession(HulaWebContext hctx)
	{
		SessionContainer sessionContainer = hctx.getSessionContainer();

		// copy contents of SessionContainer to HttpSession
		HttpSession session = hctx.getRequest().getSession(true);

		for (String key : sessionContainer.keySet())
		{
			String value = sessionContainer.get(key);
			if (value != null)
			{
				session.setAttribute(key, value);
			}
			// remove null attributes
			else
			{
				session.removeAttribute(key);
			}
		}

		// remove attributes no longer on the session
		Enumeration<String> sessionAttributes = session.getAttributeNames();
		while (sessionAttributes.hasMoreElements())
		{
			String name = sessionAttributes.nextElement();
			if (!sessionContainer.containsKey(name))
			{
				session.removeAttribute(name);
			}
		}
	}

	@Override
	public void deleteSession(HulaWebContext hctx)
	{
		// clear everything on the context
		hctx.setSessionContainer(null);
		HttpSession session = hctx.getRequest().getSession(false);
		if (session != null)
		{
			session.invalidate();
		}
	}

}
