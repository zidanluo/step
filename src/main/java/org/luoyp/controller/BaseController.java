package org.luoyp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseController
{
	private Log log = LogFactory.getLog(BaseController.class);

	@ExceptionHandler
	@ResponseBody
	public String exp(Exception ex)
	{
		log.error("Exception is caught by handler", ex);

		String msg = ex.getMessage();
		if (msg == null || "".equals(msg))
			msg = ex.getClass().getName();

		return msg;

	}
}
