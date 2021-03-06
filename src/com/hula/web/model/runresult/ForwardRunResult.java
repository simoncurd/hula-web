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
package com.hula.web.model.runresult;

import com.hula.web.model.RunResult;

/**
 * RunResult indicating that the request should be forwarded to another
 * Hula script. <br/>
 * 
 * See also {@link com.hula.web.commands.results.Forward}
 */
public class ForwardRunResult implements RunResult
{
	private String scriptName;

	public ForwardRunResult(String scriptName)
	{
		this.scriptName = scriptName;
	}

	public String getScriptName()
	{
		return scriptName;
	}

	public void setScriptName(String scriptName)
	{
		this.scriptName = scriptName;
	}

}
