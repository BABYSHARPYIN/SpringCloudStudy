package com.rio.producer.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;

public class BatchProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //1.创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("producer-group1");
        //2.设置 NameServer 的地址
        producer.setNamesrvAddr("localhost:9876");
        //3.启动producer
        producer.start();
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("BatchTest", "TagA","Id001", "你好mq".getBytes()));
        messages.add(new Message("BatchTest", "TagA","Id002", "你好mq".getBytes()));
        messages.add(new Message("BatchTest", "TagA","Id003", "你好mq".getBytes()));
        messages.add(new Message("BatchTest", "TagA","Id004", "你好mq".getBytes()));
        SendResult send = producer.send(messages);
        System.out.println(send);
    }
}
