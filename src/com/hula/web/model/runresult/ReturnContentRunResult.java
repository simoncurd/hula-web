package com.hula.web.model.runresult;

import com.hula.web.model.RunResult;

public class ReturnContentRunResult implements RunResult
{
	private String contentId;

	public ReturnContentRunResult(String contentId)
	{
		this.contentId = contentId;
	}

	public String getContentId()
	{
		return contentId;
	}

	public void setContentId(String contentId)
	{
		this.contentId = contentId;
	}

}
