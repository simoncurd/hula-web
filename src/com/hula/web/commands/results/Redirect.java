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
package com.hula.web.commands.results;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.model.NoReturnParam;
import com.hula.lang.model.RequiresParams;
import com.hula.lang.parser.model.BeanShellScript;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.HulaWebContext;
import com.hula.web.model.runresult.RedirectRunResult;

/**
 * Redirects the request onto another action, without returning. <br/>
 * <br/>
 * 
 * Example Usage: <br/>
 * <br/>
 * 
 * Redirect from the current Hula script to a Hula script called "login"<br/>
 * 
 * <pre>
 * Redirect login
 * </pre>
 * 
 * Redirect to the login Hula script, passing some parameters on the request<br/>
 * 
 * <pre>
 * Redirect login, user=jeff@hula-lang.org, message=please.login
 * </pre>
 * 
 */
@NoReturnParam
@RequiresParams(names = { "default" })
public class Redirect extends AbstractCommand
{
	private Logger logger = LoggerFactory.getLogger(Redirect.class);

	@Override
	public void execute(RuntimeConnector connector)
	{
		// default parameter is the view to go to
		String target = getVariableValueAsString("default", connector);

		// create map of parameters for redirect URL
		Map<String, String> parameters = new HashMap<String, String>();
		for (String key : this.signatureParameters.keySet())
		{
			if (key.equals("default"))
			{
				continue;
			}
			String value = getVariableValueAsString(key, connector);
			logger.debug("Redirect: mapping [{}] to [{}]", key, value);

			parameters.put(key, value);
		}

		// build run result
		RedirectRunResult redirect = new RedirectRunResult(target);
		redirect.setParameters(parameters);

		// add to context
		HulaWebContext hulaWebContext = (HulaWebContext) connector.getHulaContext();
		hulaWebContext.setRunResult(redirect);
	}

	public void updateBeanShellScript(BeanShellScript script)
	{
		super.updateBeanShellScript(script);
		script.add("return;");
	}

}
