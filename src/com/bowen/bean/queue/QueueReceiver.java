package com.bowen.bean.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.bowen.bean.MqBean;

//队列消息的接收
public class QueueReceiver {
	
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
			//创建消息的消费者对象
			consumer = session.createConsumer(destination);
			//设置消息监听
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					try {
						MqBean bean = (MqBean) ((ObjectMessage)message).getObject();
						System.out.println(bean.getName());
						if (null !=bean ||!"".equals(bean)) {
							System.out.println("收到消息" + bean.getName());
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
