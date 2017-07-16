package com.sunwoda.activemq.consumer.queue;


import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.sunwoda.activemq.sms.utils.MsgUtils;
@Component("queueMapReceiver")
public class QueueMapReceiver implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			String telephone = ((MapMessage)message).getString("telephone");
			String randomCode = ((MapMessage)message).getString("randomCode");
			System.out.println("telephone="+telephone);
			System.out.println("randomCode="+randomCode);
			MsgUtils.sendMsg(telephone, randomCode);
			System.out.println("发送成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
