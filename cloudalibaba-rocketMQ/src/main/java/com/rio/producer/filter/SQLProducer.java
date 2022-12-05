package com.rio.producer.filter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SQLProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //1.创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("producer-group-sql");
        //2.设置 NameServer 的地址
        producer.setNamesrvAddr("localhost:9876");
        //3.启动producer
        producer.start();
        for (int i = 0; i < 10; i++) {
            String s = new Date().toLocaleString();
            Message message = new Message("TopicTest", "TagA", s.getBytes());
            //给消息对象设置属性
            message.putUserProperty("i", String.valueOf(i));
            SendResult send = producer.send(message);
            System.out.println("消息id:"+send.getMsgId()+" 发送状态:"+send.getSendStatus());
            TimeUnit.SECONDS.sleep(1);
        }
        producer.shutdown();
    }
}
