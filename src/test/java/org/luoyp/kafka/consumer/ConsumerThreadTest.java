package org.luoyp.kafka.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luoyp.StepUser;
import org.luoyp.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-dao.xml", "classpath:spring-mvc.xml", "classpath:spring-service.xml" })
public class ConsumerThreadTest
{
	@Autowired
	UserDao userDao;

	@Test
	public void testRun() throws Exception
	{
		StepUser user = new StepUser();
		user.setUserId("abc");
		user.setIssueDate(new Date());
		user.setLicNo("lliicc");
		user.setAge(12);
		user.setAmount(new BigDecimal(3.14f));
		userDao.save(user);
	}
}