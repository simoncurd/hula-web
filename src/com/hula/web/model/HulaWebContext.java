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
package com.hula.web.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hula.lang.runtime.HulaContext;

/**
 * Adds web-specific attributes to the HulaContext.
 */
public class HulaWebContext extends HulaContext
{

	private SessionContainer sessionContainer;
	private RunResult runResult;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public RunResult getRunResult()
	{
		return runResult;
	}

	public void setRunResult(RunResult runResult)
	{
		this.runResult = runResult;
	}

	public SessionContainer getSessionContainer()
	{
		return sessionContainer;
	}

	public void setSessionContainer(SessionContainer sessionContainer)
	{
		this.sessionContainer = sessionContainer;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	
}
