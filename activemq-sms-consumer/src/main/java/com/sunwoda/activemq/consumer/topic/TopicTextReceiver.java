package com.sunwoda.activemq.consumer.topic;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;
@Component("topicTextReceiver")
public class TopicTextReceiver implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println(((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
