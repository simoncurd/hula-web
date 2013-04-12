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

import java.util.HashMap;
import java.util.Map;

public class ParameterUtil
{
	
	/**
	 * 
	 * FIXME: There must be a more Tomcatty way to do this..not ideal
	 * 
	 * @param paramStr
	 * @return
	 */
	public static Map<String, String[]> createParameterMap(String paramStr)
	{

		Map<String, String[]> items = new HashMap<String, String[]>();
		String[] pairs = paramStr.split("&");
		for (String pair : pairs)
		{
			String[] item = pair.split("=");
			items.put(item[0], new String[] { item[1] });
		}
		return items;
	}
	

	
	public static Map<String,String> convertParameterMap(Map<String,String[]> params)
	{
		Map<String,String> result = new HashMap<String,String>(params.size());
		for (String id : params.keySet())
		{
			String value = null;
			
			String[] values = params.get(id);
			if (values.length == 1)
			{
				value = values[0];
			}
			else
			{
				StringBuilder builder = new StringBuilder();
				for (String item : values)
				{
					if (builder.length() != 0)
					{
						builder.append(",");
					}
					builder.append(item);
				}
				value = builder.toString();
			}

			result.put(id, value);
		}
		return result;
	}
}
