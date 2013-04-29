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
import com.hula.lang.model.NoReturnParam;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.HulaWebContext;
import com.hula.web.service.session.SessionService;

/**
 * Delete the session. Session attributes will no longer be accessible. <br/>
 * <br/>
 * 
 * Example usage:<br/>
 * <br/>
 * 
 * Delete a session:<br/>
 * <pre>
 * DeleteSession
 * </pre>
 * 
 */
@NoReturnParam
public class DeleteSession extends AbstractCommand
{

	@Override
	public void execute(RuntimeConnector connector)
	{
		SessionService sessionService = connector.getService(SessionService.class);

		HulaWebContext hulaWebContext = (HulaWebContext) connector.getHulaContext();

		sessionService.deleteSession(hulaWebContext);
	}

}
