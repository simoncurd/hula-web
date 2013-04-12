package com.hula.web.commands.results;

import com.hula.lang.commands.AbstractCommand;
import com.hula.lang.model.NoReturnParam;
import com.hula.lang.model.RequiresParams;
import com.hula.lang.parser.model.BeanShellScript;
import com.hula.lang.runtime.RuntimeConnector;
import com.hula.web.model.WebHulaContext;
import com.hula.web.model.runresult.ShowPageRunResult;

@NoReturnParam
@RequiresParams(names = { "default" })
public class SetView extends AbstractCommand
{

	@Override
	public void execute(RuntimeConnector connector)
	{
		String view = this.getSignatureParameter("default");
		WebHulaContext whc = (WebHulaContext)connector.getHulaContext();
		whc.setRunResult(new ShowPageRunResult(view));
	}

	@Override
	public void updateBeanShellScript(BeanShellScript script)
	{
		super.updateBeanShellScript(script);
		script.add("return;");
	}

}
