package org.luoyp.kafka.producer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ProducerThread implements Runnable
{
	private Properties props;
	private String topic;
	private int count;
	private Integer key;
	private String message;

	private Log log = LogFactory.getLog(ProducerThread.class);

	public ProducerThread(Properties props, String topic, int count, Integer key, String message)
	{
		this.props = props;
		this.topic = topic;
		this.count = count;
		this.key = key;
		this.message = message;
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);
		ProducerRecord<Integer, String> data = new ProducerRecord<>(topic, key, message);

		for (int i = 0; i < count; i++)
		{
			producer.send(data, new Callback()
			{
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e)
				{
					if (recordMetadata == null)
						log.error(e.getMessage(), e);
				}
			});
		}

		long interval = System.currentTimeMillis() - startTime;

		log.debug(String.format("%1$d messages send in %2$d", count, interval));

	}
}
