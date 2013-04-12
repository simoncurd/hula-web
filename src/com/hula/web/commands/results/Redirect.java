package com.hula.web.commands.results;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.model.NoReturnParam;
import com.hula.lang.model.RequiresParams;
import com.hula.lang.parser.model.BeanShellScript;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.WebHulaContext;
import com.hula.web.model.runresult.RedirectRunResult;

@NoReturnParam
@RequiresParams(names = { "default" })
public class Redirect extends AbstractCommand
{
	private Logger logger = LoggerFactory.getLogger(Redirect.class);

	@Override
	public void execute(RuntimeConnector connector)
	{
		// default param is the view to go to
		String target = getVariableValueAsString("default", connector);

		RedirectRunResult redirect = new RedirectRunResult(target);
		Map<String, String[]> resolved = new HashMap<String, String[]>();
		for (String key : this.signatureParameters.keySet())
		{
			if (key.equals("default"))
			{
				continue;
			}
			String value = getVariableValueAsString(key, connector);
			logger.info("Redirect: mapping [{}] to [{}]", key, value);

			// FIXME: why is this a String[] ?
			resolved.put(key, new String[] { value });
		}
		redirect.setParameters(resolved);

		WebHulaContext whc = (WebHulaContext)connector.getHulaContext();
		whc.setRunResult(redirect);

	}

	public void updateBeanShellScript(BeanShellScript script)
	{
		super.updateBeanShellScript(script);
		script.add("return;");
	}


}
