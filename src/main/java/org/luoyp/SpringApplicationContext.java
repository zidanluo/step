package org.luoyp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware
{
	private static Log log = LogFactory.getLog(SpringApplicationContext.class);

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		log.debug("Init application context");
		this.applicationContext = applicationContext;
	}

}
