package com.rio.consumer.transaction;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class TransactionConsumer {
    public static void main(String[] args) throws MQClientException {
//1. 创建消费者Consumer(DefaultMQPushConsumer),指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group-transaction");
        //设置消费者消费的重试次数
        consumer.setMaxReconsumeTimes(3);
        //2. 指定NameServer地址
        consumer.setNamesrvAddr("localhost:9876");
        //3. 订阅(subscribe)主题 Topic
        /*
        bySql消息过滤: https://rocketmq.apache.org/zh/docs/featureBehavior/07messagefilter
        */
        consumer.subscribe("TransactionTopic", "TagA");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : list) {
                    //获取消息内容
                    String content = new String(messageExt.getBody());
                    System.out.println("消息id:" + messageExt.getMsgId() + " 消息内容:" + content);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
