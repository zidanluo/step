package org.luoyp.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.luoyp.TestPojo;
import org.luoyp.kafka.consumer.ConsumerNewApi;
import org.luoyp.kafka.consumer.StepConsumerGroup;
import org.luoyp.kafka.producer.FixedProducer;
import org.luoyp.kafka.producer.ProducerThread;
import org.luoyp.kafka.producer.StepProducerConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/kafka")
public class KafkaController extends BaseController
{
	private Log log = LogFactory.getLog(KafkaController.class);

	//	@RequestMapping(value = "/produce")
	//	@ResponseBody
	//	public String produce(String topic, Integer key, String msg)
	//	{
	//		KafkaProducer<Integer, String> producer = null;
	//		try
	//		{
	//			producer = StepProducerConfig.getProducer();
	//			// 产生并发送消息
	//			final long startTime = System.currentTimeMillis();
	//
	//			//如果topic不存在，则会自动创建，默认replication-factor为1，partitions为0
	//			ProducerRecord<Integer, String> data = new ProducerRecord<>(topic, key, msg);
	//			producer.send(data, new Callback()
	//			{
	//				@Override
	//				public void onCompletion(RecordMetadata recordMetadata, Exception e)
	//				{
	//					long elapsedTime = System.currentTimeMillis() - startTime;
	//					if (recordMetadata != null)
	//						log.info("message sent to partition(" + recordMetadata.partition() + "), " + "offset(" + recordMetadata.offset() + ") in " + elapsedTime + " ms");
	//					else
	//						log.error(e.getMessage(), e);
	//				}
	//			});
	//
	//			long i = System.currentTimeMillis() - startTime;
	//
	//			log.debug("time cost: " + i);
	//
	//			return String.valueOf(i);
	//		}
	//		catch (Exception ex)
	//		{
	//			log.error(ex.getMessage(), ex);
	//			throw ex;
	//		}
	//	}

	@RequestMapping(value = "/produce")
	@ResponseBody
	public String produce(Integer count)
	{
		long startTime = System.currentTimeMillis();

		KafkaProducer<Integer, String> producer;
		if (!FixedProducer.getListProducers().isEmpty())
			producer = FixedProducer.getListProducers().get(0);
		else
		{
			Properties props = StepProducerConfig.getProps();
			producer = new KafkaProducer<>(props);
			FixedProducer.getListProducers().add(producer);
		}

		int par = 6;

		Calendar cal = Calendar.getInstance();

		TestPojo test = new TestPojo();

		for (int i = 0; i < count; i++)
		{

			test.setUserId(String.valueOf(cal.getTimeInMillis()) + UUID.randomUUID().toString().substring(0, 6));
			test.setAge(cal.get(Calendar.SECOND));
			test.setAmount(cal.get(Calendar.SECOND) / 16f);
			test.setLicNo(String.valueOf(cal.get(Calendar.MINUTE)));
			test.setIssueDate(cal.getTime());

			final String message = JSON.toJSONString(test);

			ProducerRecord<Integer, String> data = new ProducerRecord<>("lic", i, message);

			producer.send(data, new Callback()
			{
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e)
				{
					if (recordMetadata == null)
						log.error(e.getMessage(), e);
					//					else
					//					{
					//						log.info("Message send successful: " + message);
					//					}
				}
			});
		}

		long interval = System.currentTimeMillis() - startTime;

		log.debug(String.format("%1$d messages send in %2$d", count, interval));

		return "sent";
	}

	@RequestMapping(value = "/batchProduce")
	@ResponseBody
	public String produce(String topic, Integer numThreads, Integer count)
	{
		Properties props = StepProducerConfig.getProps();
		//		props.put("bootstrap.servers", "192.168.6.133:19092,192.168.6.133:29092,192.168.6.133:9092");
		//		props.put("client.id", String.valueOf(Thread.currentThread().getId()));
		//		props.put("partitioner.class", "org.luoyp.StepPartitioner");
		//		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		//		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		String message = "test";

		for (int i = 0; i < numThreads; i++)
		{
			ProducerThread t = new ProducerThread(props, topic, count, i, message);
			executorService.submit(t);
		}
		return "started";
	}

	@RequestMapping(value = "/consume")
	@ResponseBody
	public String consume(String topic)
	{
		ConsumerNewApi consumer = new ConsumerNewApi(topic);
		consumer.start();

		return "started";
	}

	@RequestMapping(value = "/consumeGroup")
	@ResponseBody
	public String consumeGroup(String topic, String threads, String groupId) throws InterruptedException
	{
		int t = Integer.valueOf(threads);
		StepConsumerGroup.runConsumerGroup(groupId, topic, t);

		return "started";
	}

	@RequestMapping(value = "/consumeShutdown")
	@ResponseBody
	public String consumeShutdown()
	{
		StepConsumerGroup.shutdown();
		return "shutdown";
	}

}
