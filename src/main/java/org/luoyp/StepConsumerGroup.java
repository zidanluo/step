package org.luoyp;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.luoyp.controller.ConsumerThread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StepConsumerGroup
{
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;

	private Log log = LogFactory.getLog(StepConsumerGroup.class);

	public StepConsumerGroup(String a_zookeeper, String a_groupId, String a_topic)
	{
		consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper, a_groupId));
		this.topic = a_topic;
	}

	public void shutdown()
	{
		if (consumer != null)
			consumer.shutdown();
		if (executor != null)
			executor.shutdown();
		try
		{
			if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS))
			{
				log.error("Timed out waiting for consumer threads to shut down, exiting uncleanly");
			}
		}
		catch (InterruptedException e)
		{
			log.error("Interrupted during shutdown, exiting uncleanly", e);
		}
	}

	public void run(int a_numThreads)
	{
		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(topic, new Integer(a_numThreads));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		// now launch all the threads
		//
		executor = Executors.newFixedThreadPool(a_numThreads);

		// now create an object to consume the messages
		//
		int threadNumber = 0;
		for (final KafkaStream stream : streams)
		{
			executor.submit(new ConsumerThread(stream, threadNumber));
			threadNumber++;
		}
	}

	private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId)
	{
		Properties props = new Properties();
		props.put("zookeeper.connect", a_zookeeper);
		props.put("group.id", a_groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

		return new ConsumerConfig(props);
	}

	public static void main(String[] args)
	{
		String zooKeeper = "192.168.6.133:2181";
		String groupId = "groupTest";
		String topic = "beg";
		int threads = 3;

		StepConsumerGroup example = new StepConsumerGroup(zooKeeper, groupId, topic);
		example.run(threads);

		try
		{
			Thread.sleep(10000);
		}
		catch (InterruptedException ie)
		{

		}
		example.shutdown();
	}
}