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
package com.hula.web.service.script;

import com.hula.lang.reader.ScriptReader;
import com.hula.web.model.Script;
import com.hula.web.service.script.exception.ScriptNotFoundException;
import com.hula.web.service.script.exception.ScriptParseException;

/**
 * Service for resolving Hula scripts
 */
public interface ScriptService extends ScriptReader
{
	/**
	 * Check if a Hula script exists for the given name
	 * 
	 * @param name The name of the script to find
	 * @return flag to indicate if the script exists
	 */
	boolean hasScript(String name);

	/**
	 * Get a Hula script
	 * 
	 * @param name The name of the script to find
	 * @return The Hula script
	 * @throws ScriptNotFoundException
	 * @throws ScriptParseException
	 */
	Script getScript(String name) throws ScriptNotFoundException, ScriptParseException;
}
