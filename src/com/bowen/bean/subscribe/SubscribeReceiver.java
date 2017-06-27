package com.bowen.bean.subscribe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.bowen.bean.MqBean;

//订阅消息的接收
public class SubscribeReceiver {

	public static void main(String[] args) {
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接  
		Connection connection = null;
		// Session： 一个发送或接收消息的线程  
		Session session;
		// Destination ：消息的目的地;消息发送给谁.  
		Destination destination;
		// 消费者，消息接收者  
		MessageConsumer consumer;
		connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://127.0.0.1:61616");
		try {
			// 构造从工厂得到连接对象  
			connection = connectionFactory.createConnection();
			// 启动  
			connection.start();
			// 获取操作连接  
			session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置  
			destination = session.createQueue("test-queue");
			consumer = session.createConsumer(destination);
			 boolean isContinue = true;
			 while(isContinue){
				 TextMessage msg=(TextMessage) consumer.receive();
				 if (msg.getText().equals("end")) {
		                isContinue = false;
		                System.out.println("收到退出消息，程序要退出！");
		            } 
			 }
			  System.out.println("程序退出了！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
