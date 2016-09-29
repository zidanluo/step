package org.luoyp.controller;

import org.junit.Test;
import org.luoyp.TestPojo;

import java.util.Calendar;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/9/27.
 */
public class KafkaControllerTest
{

	@Test
	public void testProduce() throws Exception
	{
		Calendar cal = Calendar.getInstance();

		System.out.println(String.valueOf(cal.getTimeInMillis()) + UUID.randomUUID().toString().substring(0, 6));

	}

	@Test
	public void testProduce1() throws Exception
	{

	}

	@Test
	public void testConsume() throws Exception
	{

	}

	@Test
	public void testConsumeGroup() throws Exception
	{

	}

	@Test
	public void testConsumeShutdown() throws Exception
	{

	}
}