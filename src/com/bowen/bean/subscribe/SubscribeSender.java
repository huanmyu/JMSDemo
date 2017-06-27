package com.bowen.bean.subscribe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.bowen.bean.MqBean;

// 订阅消息的发送
public class SubscribeSender {

	public static void main(String[] args) {
		ConnectionFactory connectionFactory;
		Connection connection;
		Session session;
		Destination destination;
		MessageProducer producer;
		connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://127.0.0.1:61616");
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createTopic("test-topic");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			for(int i=0;i<100;i++){
				Thread.sleep(1000);
				TextMessage tm=session.createTextMessage("坚持的第"+i+"天");
				System.out.println("发送的消息是："+tm.getText());
				producer.send(tm);
			}
			producer.close();
			System.out.println("消息发送完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
