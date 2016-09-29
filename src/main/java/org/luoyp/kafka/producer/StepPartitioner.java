package org.luoyp.kafka.producer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class StepPartitioner implements Partitioner
{
	private Log log = LogFactory.getLog(StepPartitioner.class);

	private final AtomicInteger counter = new AtomicInteger(new Random().nextInt());

	public StepPartitioner()
	{
//		log.info("step partitioner");
	}

	/**
	 * Compute the partition for the given record.
	 *
	 * @param topic      The topic name
	 * @param key        The key to partition on (or null if no key)
	 * @param keyBytes   The serialized key to partition on( or null if no key)
	 * @param value      The value to partition on or null
	 * @param valueBytes The serialized value to partition on or null
	 * @param cluster    The current cluster metadata
	 */
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster)
	{

		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();

		int r;

		if (keyBytes == null)
		{
			int nextValue = counter.getAndIncrement();
			List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
			if (availablePartitions.size() > 0)
			{
				int part = StepPartitioner.toPositive(nextValue) % availablePartitions.size();
				r = availablePartitions.get(part).partition();
			}
			else
			{
				// no partitions are available, give a non-available partition
				r = StepPartitioner.toPositive(nextValue) % numPartitions;
			}
		}
		else
		{

			// hash the keyBytes to choose a partition
			r = StepPartitioner.toPositive(ByteBuffer.wrap(keyBytes).getInt()) % numPartitions;
		}
//		log.info(r);
		return r;
	}

	/**
	 * A cheap way to deterministically convert a number to a positive value. When the input is
	 * positive, the original value is returned. When the input number is negative, the returned
	 * positive value is the original value bit AND against 0x7fffffff which is not its absolutely
	 * value.
	 * <p/>
	 * Note: changing this method in the future will possibly cause partition selection not to be
	 * compatible with the existing messages already placed on a partition.
	 *
	 * @param number a given number
	 * @return a positive number.
	 */
	public static int toPositive(int number)
	{
		return number & 0x7fffffff;
	}

	/**
	 * This is called when partitioner is closed.
	 */
	@Override
	public void close()
	{

	}

	/**
	 * Configure this class with the given key-value pairs
	 */
	@Override
	public void configure(Map<String, ?> map)
	{

	}
}
