package com.hula.web.commands.results;

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.model.NoReturnParam;
import com.hula.lang.model.RequiresParams;
import com.hula.lang.parser.model.BeanShellScript;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.WebHulaContext;
import com.hula.web.model.runresult.ForwardRunResult;

/**
 * Forwards the request onto another action, without returning
 * 
 * @author simoncurd
 * 
 */
@NoReturnParam
@RequiresParams(names = { "default" })
public class Forward extends AbstractCommand
{

	@Override
	public void execute(RuntimeConnector connector)
	{
		ForwardRunResult frr = new ForwardRunResult(getSignatureParameter("default"));
		WebHulaContext whc = (WebHulaContext)connector.getHulaContext();
		whc.setRunResult(frr);

	}

	public void updateBeanShellScript(BeanShellScript script)
	{
		super.updateBeanShellScript(script);
		script.add("return;");
	}


}
