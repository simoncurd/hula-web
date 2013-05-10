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
package com.hula.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.hula.web.model.Route;

public class RoutingTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		new RoutingTest();
	}
	
	public RoutingTest()
	{
		Route route = createRoute("/*mens/*=category");
		System.out.println(route.getPattern());
		
		isMatch(route.getPattern(), "/mens/trousers");
		isMatch(route.getPattern(), "/mens/");
		isMatch(route.getPattern(), "/mens");
		isMatch(route.getPattern(), "/womens");
		isMatch(route.getPattern(), "/womens/");

		
	}
	
	
	private void isMatch(Pattern exp, String value)
	{

		//Pattern referencePattern = Pattern.compile(exp);
		Matcher m = exp.matcher(value);
		
		System.out.println("matching [" + value +"] with [" + exp + "] = " + m.matches());
	}
			
	private Route createRoute(String expression)
	{
		Route route = new Route();
		
		String[] parts = expression.split("=");
		
		// set the script
		route.setScript(parts[1]);
		
		// set the pattern to match
		String match = parts[0];
		match = match.replaceAll("\\*", "(\\.\\*)");
		match = "^" + match + "$";
		route.setPattern(Pattern.compile(match));
		
		return route;
	}
	
	
	
}
