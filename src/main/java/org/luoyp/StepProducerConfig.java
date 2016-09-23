package org.luoyp;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * Created by Administrator on 2016/9/19.
 */
public class StepProducerConfig
{

	private static KafkaProducer<Integer, String> producer;

	static
	{
		// key.serializer.class默认为serializer.class
		//		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		// 可选配置，如果不配置，则使用默认的partitioner
		//		props.put("partitioner.class", "com.catt.kafka.demo.PartitionerDemo");
		// 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失
		// 值为0,1,-1,可以参考
		// http://kafka.apache.org/08/configuration.html
		//		props.put("request.required.acks", "1");

		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.6.133:19092,192.168.6.133:29092,192.168.6.133:9092");
		props.put("client.id", "DemoProducer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// 创建producer
		producer = new KafkaProducer<>(props);
	}

	public static KafkaProducer<Integer, String> getProducer()
	{
		return producer;
	}
}
