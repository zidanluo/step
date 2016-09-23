package org.luoyp;

//import kafka.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerConfig;

import kafka.utils.ShutdownableThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * Created by Administrator on 2016/9/19.
 */
public class StepConsumer extends ShutdownableThread
{
	private final KafkaConsumer<Integer, String> consumer;
	private final String topic;
	private Log log = LogFactory.getLog(StepConsumer.class);

	public StepConsumer(String topic)
	{
		super("KafkaConsumerExample", false);
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.6.133:19092,192.168.6.133:29092,192.168.6.133:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "StepConsumer");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

		consumer = new KafkaConsumer<>(props);
		this.topic = topic;
	}

	@Override
	public void doWork()
	{
		consumer.subscribe(Collections.singletonList(this.topic));
		ConsumerRecords<Integer, String> records = consumer.poll(1000);

		for (ConsumerRecord<Integer, String> record : records)
		{
			log.info("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset() + " from partition " + record.partition());
		}
	}

	@Override
	public String name()
	{
		return null;
	}

	@Override
	public boolean isInterruptible()
	{
		return false;
	}
}
