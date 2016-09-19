package org.luoyp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/home")
public class HomeController
{
	private Log log = LogFactory.getLog(HomeController.class);
	//	private Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String test(HttpServletRequest request)
	{
		log.debug(request.getScheme());
		log.debug(request.getServerName());
		log.debug(request.getServerPort());
		log.debug(request.getLocalAddr());
		log.debug(request.getLocalPort());

		return String.format("%1$s, %2$s, %3$s, %4$s, %5$s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getLocalAddr(), request.getLocalPort());
	}
}
