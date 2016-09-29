package org.luoyp.kafka.consumer;

import com.alibaba.fastjson.JSON;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.luoyp.SpringApplicationContext;
import org.luoyp.StepUser;
import org.luoyp.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.util.Date;

public class ConsumerThread implements Runnable
{
	private KafkaStream<byte[], byte[]> stream;

	private Log log = LogFactory.getLog(ConsumerThread.class);

	private UserDao userDao = SpringApplicationContext.getApplicationContext().getBean(UserDao.class);

	public ConsumerThread(KafkaStream<byte[], byte[]> stream)
	{
		this.stream = stream;
	}

	@Override
	public void run()
	{
		//		int i = 0;
		//		long begin = 0;
		for (MessageAndMetadata<byte[], byte[]> m : stream)
		{
			//			i++;
			//			if (i == 1)
			//			{
			//				begin = System.currentTimeMillis();
			//			}
			//			else if (i == 10000)
			//			{
			//				log.info(String.format("Thread (%1$d) consumed 10000 message from partition (%2$d) in %3$d", Thread.currentThread().getId(), m.partition(), System.currentTimeMillis() - begin));
			//				i = 0;
			//			}

			try
			{
				String value = new String(m.message());
				Integer key = ByteBuffer.wrap(m.key()).getInt();
				String topic = m.topic();
//				log.info(String.format("Thread (%1$d) consumer message (%2$s) of topic: (%5$s) - key (%4$d) from partition (%3$d) ", Thread.currentThread().getId(), value, m.partition(), key, topic));

				if ("lic".equals(m.topic()))
				{
					StepUser t = JSON.parseObject(value, StepUser.class);
					saveUser(t);
				}
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
			}
		}
		log.info("Shutting down Thread: " + Thread.currentThread().getId());
	}

	@Transactional
	private void saveUser(StepUser u)
	{
		u.setCreateDate(System.currentTimeMillis());

		userDao.save(u);
	}
}