package com.hula.web.model.runresult;

import com.hula.web.model.RunResult;

public class FailureRunResult implements RunResult
{
	private String errorCode;
	private String errorDescription;

	public FailureRunResult(String errorCode, String errorDescription)
	{
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public String getErrorDescription()
	{
		return errorDescription;
	}

}
