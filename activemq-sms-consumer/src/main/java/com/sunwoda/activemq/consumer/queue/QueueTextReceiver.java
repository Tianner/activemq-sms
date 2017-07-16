package com.sunwoda.activemq.consumer.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;
@Component("queueTextReceiver")
public class QueueTextReceiver implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println(((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
