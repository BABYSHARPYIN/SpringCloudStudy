package com.rio.consumer.filter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class SQLConsumer {
    public static void main(String[] args) throws MQClientException {
        //1. 创建消费者Consumer(DefaultMQPushConsumer),指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group-sql");
        //2. 指定NameServer地址
        consumer.setNamesrvAddr("localhost:9876");
        //3. 订阅(subscribe)主题 Topic
        /*
        bySql消息过滤: https://rocketmq.apache.org/zh/docs/featureBehavior/07messagefilter
        */
        consumer.subscribe("TopicTest", MessageSelector.bySql("i between 0 and 3"));
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : list) {
                    //获取消息内容
                    String content = new String(messageExt.getBody());
                    String i = messageExt.getUserProperty("i");
                    System.out.println("消息id:" + messageExt.getMsgId() + " 消息内容:" + content + " i:" + i);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
