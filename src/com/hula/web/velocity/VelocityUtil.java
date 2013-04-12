package com.hula.web.velocity;

import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityUtil
{

	public static void mergeTemplateWithOverride(String defaultTemplateName, String appSpecificTemplateName, VelocityContext vctx, Writer out)
	{
		// String errorPageName = "error:500.vm";

		String templateName = defaultTemplateName;
//		PageService ps = ServiceModule.getPageService();
//		try
//		{
//			Page p = ps.getPage(appSpecificTemplateName, InfrastructureDirectory.getApplication());
//			templateName = appSpecificTemplateName;
//		}
//		catch (ResourceNotFoundException e)
//		{
//			// use the default page
//		}
//		catch (ResourceServiceException e)
//		{
//			// something more generic - swallow this
//		}

		Template page = Velocity.getTemplate(templateName);
		page.merge(vctx, out);

	}

}
