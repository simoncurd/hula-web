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
package com.hula.web.runtime;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hula.web.model.RunResult;
import com.hula.web.response.ResponseProcessor;

@Singleton
public class ResponseProcessorMapping
{
	private Map<String,ResponseProcessor> processors;
	
	@Inject
	public ResponseProcessorMapping(Map<String,ResponseProcessor> processors)
	{
		this.processors = processors;
	}
	public ResponseProcessor getResponseProcessor(RunResult runResult)
	{
		return processors.get(runResult.getClass().getSimpleName());
	}
}
