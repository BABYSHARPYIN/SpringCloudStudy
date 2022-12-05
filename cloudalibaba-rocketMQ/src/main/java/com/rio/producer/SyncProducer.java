package com.rio.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 发送同步消息
 * 这种可靠性同步地发送方式使用的比较广泛,比如: 重要的消息通知,短信通知.
 */
public class SyncProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //1.创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("producer-group1");
        //2.设置 NameServer 的地址
        producer.setNamesrvAddr("localhost:9876");
        //3.启动producer
        producer.start();
        /**
         * 消息对象的构造方法
         * 参数1 主题
         * 参数2 标签
         * 参数3 消息体
         * */
        //4.构建消息对象,指定主题Topic,消息体
        //5.发送消息,消息将会持久化进 broker 中的queue队列
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Demo1Topic", "TagA", "你好mq".getBytes());
            SendResult send = producer.send(message);
            System.out.println(send);
        }
        //6.关闭生产者producer
        producer.shutdown();


    }
}
