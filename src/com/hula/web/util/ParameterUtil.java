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
