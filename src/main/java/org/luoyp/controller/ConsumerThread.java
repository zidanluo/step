package org.luoyp.controller;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConsumerThread implements Runnable
{
	private KafkaStream m_stream;
	private int m_threadNumber;

	private Log log = LogFactory.getLog(ConsumerThread.class);

	public ConsumerThread(KafkaStream a_stream, int a_threadNumber)
	{
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;
	}

	public void run()
	{
		ConsumerIterator<Integer, String> it = m_stream.iterator();
		while (it.hasNext())
		{
			MessageAndMetadata<Integer, String> m = it.next();
			log.info(String.format("Thread (%1$d) consumer message (%2$s) of key (%4$s) from partition (%3$d) ", m_threadNumber, m.message(), m.partition(), m.key()));

		}
		log.info("Shutting down Thread: " + m_threadNumber);
	}
}