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
package com.hula.web.commands.request;

import javax.servlet.http.HttpServletRequest;

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.model.RequiresParams;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.WebConstants;
import com.hula.web.model.HulaWebContext;

/**
 * Returns a segment of the request path. <br/><br/>
 * 
 * Example Usage:<br/><br/>
 * 
 * For request <code>/event/123</code>, extract the second token<br/>
 * <pre>
 * GetPathVariable index=2 as eventId
 * </pre>
 */
@RequiresParams(names="index")
public class GetPathVariable extends AbstractCommand
{

	@Override
	public void execute(RuntimeConnector connector)
	{

		HulaWebContext hulaWebContext = (HulaWebContext) connector.getHulaContext();
		HttpServletRequest request = hulaWebContext.getRequest();
		String path = (String) request.getAttribute(WebConstants.ServletPath);
		String[] parts = path.split("/");
		String indexStr = getVariableValueAsString("index", connector);
		int index = Integer.parseInt(indexStr);

		if (index > parts.length - 1)
		{
			return;
		}

		assignVariable(getReturnParameter(), parts[index], connector);
	}
}
