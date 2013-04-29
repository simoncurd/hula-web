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
package com.hula.web.velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.directive.Directive;

/**
 * Utility class with methods for initialising Velocity
 */
public class VelocityInitialiser
{
	/**
	 * Initialise Velocity
	 * 
	 * @param templatePath the path to templates
	 */
	public static final void initialise(String templatePath)
	{
		// Setup Velocity
		Properties velocityProperties = new Properties();

		// create a list of custom directives we're using
		List<Class<? extends Directive>> userDirectiveClasses = new ArrayList<Class<? extends Directive>>();
		userDirectiveClasses.add(Decorate.class);

		// build the user directive string
		StringBuilder userDirectiveStr = new StringBuilder();
		for (Class<? extends Directive> clz : userDirectiveClasses)
		{
			if (userDirectiveStr.length() > 0)
			{
				userDirectiveStr.append(",");
			}
			userDirectiveStr.append(clz.getName());
		}

		// wire in our custom directives
		velocityProperties.setProperty("userdirective", userDirectiveStr.toString());

		velocityProperties.setProperty("velocimacro.library", "");

		// wire in our custom resource loader
		velocityProperties.setProperty("resource.loader", "relativecp");
		velocityProperties.setProperty("relativecp.resource.loader.description", "Relative CP Loader");
		velocityProperties.setProperty("relativecp.resource.loader.class", RelativeClasspathResourceLoader.class.getName());
		velocityProperties.setProperty(RelativeClasspathResourceLoader.RELATIVE_PATH, templatePath);

		// initialise
		Velocity.init(velocityProperties);
	}
}
