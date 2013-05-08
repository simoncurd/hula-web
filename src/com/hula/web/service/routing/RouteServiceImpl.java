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
package com.hula.web.service.routing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hula.lang.util.FileUtil;
import com.hula.web.model.Route;

@Singleton
public class RouteServiceImpl implements RouteService
{
	private String scriptPath = null;
	private List<Route> routes;

	@Inject
	public RouteServiceImpl(@Named("script.path") String scriptPath)
	{
		this.scriptPath = scriptPath;

		try
		{
			InputStream in = FileUtil.getFileInputStream(scriptPath + "/routing.properties");

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			this.routes = new ArrayList<Route>();

			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if (line.startsWith("#") || line.equals(""))
				{
					continue;
				}
				routes.add(createRoute(line));
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("error loading hula routes", e);
		}

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

	@Override
	public Route getRoute(String servletPath)
	{
		for (Route route : routes)
		{
			Matcher matcher = route.getPattern().matcher(servletPath);
			if (matcher.matches())
			{
				return route;
			}
		}

		return null;
	}

}
