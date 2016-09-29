package org.luoyp;

import org.junit.Test;
import org.luoyp.kafka.producer.StepPartitioner;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/9/27.
 */
public class StepPartitionerTest
{

	@Test
	public void testPartition() throws Exception
	{
		byte[] keyBytes = new byte[] { (byte) 0x80, 0x00, 0x00, 0x11 };


		int b = ByteBuffer.wrap(keyBytes).getInt();
		System.out.println(b);

		int a = StepPartitioner.toPositive(b);
		System.out.println(a);
	}

	@Test
	public void testClose() throws Exception
	{
		byte t = (byte) 0x7F;
		System.out.println(t);
	}

	@Test
	public void testConfigure() throws Exception
	{

	}
}