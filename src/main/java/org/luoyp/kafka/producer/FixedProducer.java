package org.luoyp.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class FixedProducer
{
	private static List<KafkaProducer<Integer, String>> listProducers = new ArrayList<>();

	public static List<KafkaProducer<Integer, String>> getListProducers()
	{
		return listProducers;
	}
}
