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

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * A custom Velocity Resource Loader for loading templates from a path relative
 * to a classpath location. <br/>
 * <br/>
 * 
 * This is required because the Velocity ClasspathResourceLoader
 * expects templates either in the WEB-INF/classes directory, or bundled into a
 * jar file. This allows us to serve templates from an alternative folder
 * within the web app, by specifying a path relative to the WebappClassLoader.<br/>
 * <br/>
 * 
 * For example, specifying <code>relativecp.path=../scripts</code> means the
 * resource loader will look in <code>WEB-INF/classes/../scripts</code>, which resolves
 * to <code>WEB-INF/scripts</code> <br/>
 * <br/>
 * 
 * Path specification needs to happen within the ResourceLoader (as opposed to
 * when calling Velocity.getTemplate(...)) to support references within a template
 * to another template.
 */
public class RelativeClasspathResourceLoader extends ClasspathResourceLoader
{
	public static final String RELATIVE_PATH = "relativecp.path";

	private String relativePath = null;

	public void init(ExtendedProperties configuration)
	{
		super.init(configuration);

		Object obj = Velocity.getProperty(RELATIVE_PATH);
		if (obj != null)
		{
			relativePath = obj.toString();
		}
	}

	public InputStream getResourceStream(String name) throws ResourceNotFoundException
	{
		return super.getResourceStream(relativePath + "/" + name);
	}
}
