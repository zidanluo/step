package org.luoyp.controller;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.zookeeper.ClientCnxn;
import org.luoyp.StepConsumer;
import org.luoyp.StepConsumerGroup;
import org.luoyp.StepProducerConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/kafka")
public class KafkaController extends BaseController
{
	private Log log = LogFactory.getLog(KafkaController.class);

	@RequestMapping(value = "/produce")
	@ResponseBody
	public String produce(String topic, Integer key, String msg)
	{
		KafkaProducer<Integer, String> producer = null;
		try
		{
			producer = StepProducerConfig.getProducer();
			// 产生并发送消息
			final long startTime = System.currentTimeMillis();

			//如果topic不存在，则会自动创建，默认replication-factor为1，partitions为0
			ProducerRecord<Integer, String> data = new ProducerRecord<>(topic, key, msg);
			producer.send(data, new Callback()
			{
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e)
				{
					long elapsedTime = System.currentTimeMillis() - startTime;
					if (recordMetadata != null)
						log.info("message sent to partition(" + recordMetadata.partition() + "), " + "offset(" + recordMetadata.offset() + ") in " + elapsedTime + " ms");
					else
						log.error(e.getMessage(), e);
				}
			});

			long i = System.currentTimeMillis() - startTime;

			log.debug("time cost: " + i);

			return String.valueOf(i);
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@RequestMapping(value = "/consume")
	@ResponseBody
	public String consume(String topic)
	{
		StepConsumer consumer = new StepConsumer(topic);
		consumer.start();
		//		consumer.stop();
		return "started";
	}

	@RequestMapping(value = "/consumeGroup")
	@ResponseBody
	public String consumeGroup(String topic, String threads) throws InterruptedException
	{
		String zooKeeper = "192.168.6.133:2181";
		String groupId = "groupTest";
		int t = Integer.valueOf(threads);

		StepConsumerGroup example = new StepConsumerGroup(zooKeeper, groupId, topic);
		example.run(t);

//		try
//		{
//			Thread.sleep(120000);
//		}
//		catch (InterruptedException ie)
//		{
//			log.error(ie.getMessage(), ie);
//			throw ie;
//		}
//		example.shutdown();
		return "started";
	}

}
