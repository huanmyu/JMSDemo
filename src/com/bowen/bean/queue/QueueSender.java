package com.bowen.bean.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.bowen.bean.MqBean;

//队列消息的发送:
public class QueueSender {
	public static void main(String[] args) {
		ConnectionFactory connectionFactory;
		Connection connection;
		Session session;
		Destination destination;
		MessageProducer producer;
		//创建连接工厂
		connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://127.0.0.1:61616");
		try {
			//创建连接对象
			connection = connectionFactory.createConnection();
			//开始建立连接
			connection.start();
			//第一个参数是是否是事务型消息，设置为true,第二个参数无效
			//第二个参数是
			//Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。异常也会确认消息，应该是在执行之前确认的
			//Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会删除消息。可以在失败的
			//时候不确认消息,不确认的话不会移出队列，一直存在，下次启动继续接受。接收消息的连接不断开，其他的消费者也不会接受（正常情况下队列模式不存在其他消费者）
			//DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效。
			//待测试
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			//创建队列名是test-queue的队列源
			destination = session.createQueue("test-queue");
			//创建队列的生成者
			producer = session.createProducer(destination);
			//设置不持久化
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			MqBean bean = new MqBean();
			bean.setAge(13);
			for(int i=0;i<100;i++){
				bean.setName("小黄"+i);
				//发送对象消息
				ObjectMessage objectMessage=session.createObjectMessage(bean);
				MqBean m=(MqBean) objectMessage.getObject();
				System.out.println("发送的对象信息是："+m.getName());
				producer.send(objectMessage);
				//10秒发送下一条消息
				Thread.sleep(100000);
			}
			producer.close();
			System.out.println("消息发送完毕");
		} catch (JMSException e) {
			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
