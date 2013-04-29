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

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.model.NoReturnParam;
import com.hula.lang.model.RequiresParams;
import com.hula.lang.parser.model.BeanShellScript;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.HulaWebContext;
import com.hula.web.model.runresult.ForwardRunResult;

/**
 * Forwards the request onto another action, without returning. <br/>
 * <br/>
 * 
 * Example Usage: <br/>
 * <br/>
 * 
 * Forwards from the current Hula script to a Hula script called "login" <br/>
 * 
 * <pre>
 * Forward login
 * </pre>
 * 
 */
@NoReturnParam
@RequiresParams(names = { "default" })
public class Forward extends AbstractCommand
{
	@Override
	public void execute(RuntimeConnector connector)
	{
		ForwardRunResult forwardRunResult = new ForwardRunResult(getSignatureParameter("default"));
		HulaWebContext hulaWebContext = (HulaWebContext) connector.getHulaContext();
		hulaWebContext.setRunResult(forwardRunResult);
	}

	public void updateBeanShellScript(BeanShellScript script)
	{
		super.updateBeanShellScript(script);
		script.add("return;");
	}
}
