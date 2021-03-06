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
package com.hula.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterUtils
{
	public static Map<String,Object> convertParameterMap(Map<String,String[]> params)
	{
		Map<String,Object> result = new HashMap<String,Object>(params.size());
		for (String id : params.keySet())
		{
			Object value = null;
			
			String[] values = params.get(id);
			if (values.length == 1)
			{
				value = values[0];
			}
			else
			{
				List<String> valueList = new ArrayList<String>(values.length);
				for (String item : values)
				{
					valueList.add(item);
				}
				value = valueList;
			}

			result.put(id, value);
		}
		return result;
	}


}
