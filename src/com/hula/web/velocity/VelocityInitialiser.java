package com.hula.web.velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityInitialiser
{
	public static final void initialise(String applicationPath)
	{
		// Setup Velocity
		Properties p = new Properties();

		// create a list of custom directives we're using
		List<Class> userDirectiveClasses = new ArrayList<Class>();
		userDirectiveClasses.add(Decorate.class);

		// build the user directive string
		StringBuilder userDirectiveStr = new StringBuilder();
		for (Class clz : userDirectiveClasses)
		{
			if (userDirectiveStr.length() > 0)
			{
				userDirectiveStr.append(",");
			}
			userDirectiveStr.append(clz.getName());
		}
		
		System.out.println("directive [" + userDirectiveStr.toString() + "]");
		// wire in our custom directive
		p.setProperty("userdirective", userDirectiveStr.toString());
		
		p.setProperty("velocimacro.library", "");

		// tell velocity where our .vm files live
		
		p.setProperty("resource.loader", "relativecp");
		p.setProperty("relativecp.resource.loader.description", "Relative CP Loader");
		p.setProperty("relativecp.resource.loader.class", RelativeClasspathResourceLoader.class.getName());
		p.setProperty(RelativeClasspathResourceLoader.RELATIVE_PATH, applicationPath);
		
		System.out.println("Velocty class loader: " + ClasspathResourceLoader.class.getClassLoader());
		
		
		Velocity.init(p);
	}
}
