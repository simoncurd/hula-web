package com.hula.web.model.runresult;

import java.util.Map;

import com.hula.web.model.RunResult;

public class RedirectRunResult implements RunResult
{
	private String action;

	private Map<String, String[]> parameters;

	public RedirectRunResult(String action)
	{
		this.action = action;
	}

	public String getAction()
	{
		return action;
	}

	public Map<String, String[]> getParameters()
	{
		return parameters;
	}

	public void setParameters(Map<String, String[]> parameters)
	{
		this.parameters = parameters;
	}

}
