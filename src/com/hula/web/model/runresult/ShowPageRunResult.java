package com.hula.web.model.runresult;

import com.hula.web.model.RunResult;

public class ShowPageRunResult implements RunResult
{
	private String pageName;

	public ShowPageRunResult(String pageName)
	{
		this.pageName = pageName;
	}

	public String getPageName()
	{
		return pageName;
	}

}
