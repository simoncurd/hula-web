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
package com.hula.web.commands.session;

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.HulaWebContext;
import com.hula.web.model.SessionContainer;
import com.hula.web.service.session.SessionService;

/**
 * Gets a reference to the session. <br/>
 * <br/>
 * 
 * Example usage:<br/>
 * <br/>
 * 
 * Find an existing session, or create a new one:<br/>
 * <pre>
 * GetSession as session
 * </pre>
 * 
 * Find an existing session, but don't create a new one: <br/>
 * <pre>
 * GetSession create=false as session
 * </pre>
 * 
 */
//@FailsWith(errorCodes = { "missing.session" })
public class GetSession extends AbstractCommand
{

	@Override
	public void execute(RuntimeConnector connector)
	{
		SessionService sessionService = connector.getService(SessionService.class);
		
		// by default we'll create a session if one doesn't exist
		boolean createSession = true;

		// check if the user has explicitly said 'don't create a session'
		String createSessionStr = getVariableValueAsString("create", connector);
		if (createSessionStr != null && !createSessionStr.equals(""))
		{
			createSession = Boolean.parseBoolean(createSessionStr);
		}
		
		// check if we've already got the session
		HulaWebContext hulaWebContext = (HulaWebContext) connector.getHulaContext();
		SessionContainer sessionContainer = hulaWebContext.getSessionContainer();
		if (sessionContainer == null)
		{	
			// find or create
			sessionContainer = sessionService.getSession(createSession, hulaWebContext);
			
			// fail if we haven't created one
			if (sessionContainer == null && !createSession)
			{
				//throw new HulaRuntimeException("missing.session", "session not available");
				return;
			}
		}
		
		if (getReturnParameter() != null)
		{
			assignVariable(getReturnParameter(), sessionContainer, connector);
		}
	}

}
