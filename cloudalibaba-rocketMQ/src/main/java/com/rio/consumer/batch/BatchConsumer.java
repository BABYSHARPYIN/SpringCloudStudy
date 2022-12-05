package com.rio.consumer.batch;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class BatchConsumer {
    public static void main(String[] args) throws MQClientException {
        //1. 创建消费者Consumer(DefaultMQPushConsumer),指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group1");
        //2. 指定NameServer地址
        consumer.setNamesrvAddr("localhost:9876");
        //3. 订阅(subscribe)主题 Topic
        consumer.subscribe("BatchTest", "TagA");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    for (MessageExt messageExt : list) {
                        //获取消息的 topic
                        String topic = messageExt.getTopic();
                        //获取消息的标签
                        String tags = messageExt.getTags();
                        //获取消息的key(业务键)
                        String keys = messageExt.getKeys();
                        //获取消息内容
                        String content = new String(messageExt.getBody());
                        System.out.println("QueueId=" + messageExt.getQueueId() + " topic=" + topic + " tags=" + tags + " keys=" + keys + " content=" + content);
                    }
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
