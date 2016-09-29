package org.luoyp.kafka.consumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerThreadId;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.consumer.ConsumerRebalanceListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StepConsumerGroup
{
	private static ExecutorService executor;

	private static Log log = LogFactory.getLog(StepConsumerGroup.class);

	private static List<ConsumerConnector> listConsumers;

	static
	{
		executor = Executors.newCachedThreadPool();
		listConsumers = new ArrayList<>();
	}

	public static void runConsumerGroup(String groupId, String topic, int numThreads)
	{
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(groupId));
		consumer.setConsumerRebalanceListener(new ConsumerRebalanceListener()
		{
			@Override
			public void beforeReleasingPartitions(Map<String, Set<Integer>> partitionOwnership)
			{
				//				log.info(partitionOwnership);
			}

			@Override
			public void beforeStartingFetchers(String consumerId, Map<String, Map<Integer, ConsumerThreadId>> globalPartitionAssignment)
			{
				//				log.info(consumerId);
				//				log.info(globalPartitionAssignment);
			}
		});
		listConsumers.add(consumer);

		Map<String, Integer> topicCountMap = new HashMap<>();
		topicCountMap.put(topic, numThreads);

		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		for (KafkaStream<byte[], byte[]> stream : streams)
		{
			executor.submit(new ConsumerThread(stream));
		}

	}

	public static void shutdown()
	{
		try
		{
			for (ConsumerConnector consumer : listConsumers)
			{
				if (consumer != null)
					consumer.shutdown();
			}
			if (executor != null)
				executor.shutdown();

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

	private static ConsumerConfig createConsumerConfig(String groupId)
	{
		Properties props = new Properties();
		props.put("zookeeper.connect", "192.168.6.133:2181");
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		//		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
		//		props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

		return new ConsumerConfig(props);
	}
}