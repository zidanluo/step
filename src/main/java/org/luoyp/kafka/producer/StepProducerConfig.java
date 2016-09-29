package org.luoyp.kafka.producer;

import java.util.Properties;

/**
 * Created by Administrator on 2016/9/27.
 */
public class StepProducerConfig
{
	private static Properties props;

	public static Properties getProps()
	{
		return props;
	}

	static
	{
		props = new Properties();
		props.put("bootstrap.servers", "192.168.6.133:19092,192.168.6.133:29092,192.168.6.133:9092");
//		props.put("client.id", String.valueOf(Thread.currentThread().getId()));
		props.put("partitioner.class", "org.luoyp.kafka.producer.StepPartitioner");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	}

}
