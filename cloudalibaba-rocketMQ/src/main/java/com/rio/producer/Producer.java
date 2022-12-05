package com.rio.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * @Date 2022年11月24日
 * @Description 生产者消费者 入门案例
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //1.创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("producer-group1");
        //2.设置 NameServer 的地址
        producer.setNamesrvAddr("localhost:9876");
        //3.启动producer
        producer.start();

        //4.构建消息对象,指定主题Topic,消息体
        /**
         * 消息对象的构造方法
         * 参数1 主题
         * 参数2 标签
         * 参数3 消息体
         * */
        //5.发送消息,消息将会持久化进 broker 中的queue队列
        for (int i = 0; i < 100; i++) {
            Message message = new Message("Demo1Topic", "Demo1Tag", String.format("你好mq: %s", i).getBytes());
            /**设置消息的延时级别
             * 默认延时级别(可以在broker中修改配置文件):1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
             * */
            message.setDelayTimeLevel(3);
            /**不指定发送的queue,不是全局唯一*/
//            SendResult send = producer.send(message);
            /**指定 Queue 发送消息,全局顺序生产消息*/
            SendResult send = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mgs, Message message, Object arg) {
                    System.out.println(mgs.size());//broker中的queue数
                    int index = (Integer) arg;
                    return mgs.get(index);//指定MessageQueueList 中的第几个 Queue
                }
            }, 0);
            System.out.println(send);
        }
        //6.关闭生产者producer
        producer.shutdown();
    }
}
