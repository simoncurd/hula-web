/**
 * Copyright 2013 Simon Curd <simoncurd@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hula.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.hula.web.model.runresult.RedirectRunResult;
import com.hula.web.util.URLUtils;

public class URLUtilsTests
{

	@Test
	public void testGetRedirectURL_relative()
	{
		RedirectRunResult rrr = new RedirectRunResult("login");

		String result = URLUtils.getRedirectURL(rrr, "http://localhost/hula/checkout", "/hula");

		Assert.assertEquals("incorrect result url", "http://localhost/hula/login", result);
	}
	

	@Test
	public void testGetRedirectURL_relativeWithPort()
	{
		RedirectRunResult rrr = new RedirectRunResult("login");

		String result = URLUtils.getRedirectURL(rrr, "http://localhost:9001/hula/checkout", "/hula");

		Assert.assertEquals("incorrect result url", "http://localhost:9001/hula/login", result);
	}

	@Test
	public void testGetRedirectURL_relativeWithParams()
	{
		RedirectRunResult rrr = new RedirectRunResult("login");
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("name", "Jeff");
		params.put("age", "32");
		rrr.setParameters(params);

		String result = URLUtils.getRedirectURL(rrr, "http://localhost:9001/hula/checkout", "/hula");

		Assert.assertEquals("incorrect result url", "http://localhost:9001/hula/login?name=Jeff&age=32", result);
	}

	@Test
	public void testGetRedirectURL_absolute()
	{
		RedirectRunResult rrr = new RedirectRunResult("http://www.google.com");

		String result = URLUtils.getRedirectURL(rrr, "http://localhost:9001/hula/checkout", "/hula");

		Assert.assertEquals("incorrect result url", "http://www.google.com", result);
	}

	@Test
	public void testGetScriptName_withContext()
	{
		String result = URLUtils.getScriptName("http://localhost:9001/hula/welcome", "/hula");
		Assert.assertEquals("incorrect script", "welcome", result);
	}
	

	@Test
	public void testGetScriptName_withContextAndFolder()
	{
		String result = URLUtils.getScriptName("http://localhost:9001/hula/checkout/welcome", "/hula");
		Assert.assertEquals("incorrect script", "checkout/welcome", result);
	}
	

	@Test
	public void testGetScriptName()
	{
		String result = URLUtils.getScriptName("http://localhost:9001/welcome", "");
		Assert.assertEquals("incorrect script", "welcome", result);
	}
}
