package com.hula.web.model.runresult;

import com.hula.web.model.RunResult;

public class ForwardRunResult implements RunResult
{
	private String scriptName;

	public ForwardRunResult(String scriptName)
	{
		this.scriptName = scriptName;
	}

	public String getScriptName()
	{
		return scriptName;
	}

	public void setScriptName(String scriptName)
	{
		this.scriptName = scriptName;
	}


}
