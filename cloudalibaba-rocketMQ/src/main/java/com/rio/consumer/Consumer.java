package com.rio.consumer;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class Consumer {
    public static void main(String[] args) throws MQClientException {
        //1. 创建消费者Consumer(DefaultMQPushConsumer),指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group1");
        //2. 指定NameServer地址
        consumer.setNamesrvAddr("localhost:9876");
        //3. 订阅(subscribe)主题 Topic
        consumer.subscribe("Demo1Topic", "Demo1Tag");
//        consumer.setMessageModel(MessageModel.BROADCASTING); //广播模式消费(所有消费者都会收到消息)
        consumer.setMessageModel(MessageModel.CLUSTERING); //集群模式消费(集群模式下只有一台机器会收到消息)

        //4. 注册监听,指定回调函数
        /**监听同步消息,不保证顺序性*/
        /*consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    int c = 0;
                    for (MessageExt messageExt : list) {
                        //获取消息的 topic
                        String topic = messageExt.getTopic();
                        //获取消息的标签
                        String tags = messageExt.getTags();
                        //获取消息内容
                        String content = new String(messageExt.getBody());
                        System.out.println("QueueId="+messageExt.getQueueId()+" order=" + c++ + " topic=" + topic + " tags=" + tags + " content=" + content);
                    }
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });*/

        /**监听顺序消息,保证消息顺序*/
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                try {
                    int c = 0;
                    for (MessageExt messageExt : list) {
                        //获取消息的 topic
                        String topic = messageExt.getTopic();
                        //获取消息的标签
                        String tags = messageExt.getTags();
                        //获取消息内容
                        String content = new String(messageExt.getBody());
                        System.out.println("QueueId="+messageExt.getQueueId()+" order=" + c++ + " topic=" + topic + " tags=" + tags + " content=" + content);
                    }
                } catch (Exception e) {
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;

            }
        });

        //启动消费者
        consumer.start();


    }
}
