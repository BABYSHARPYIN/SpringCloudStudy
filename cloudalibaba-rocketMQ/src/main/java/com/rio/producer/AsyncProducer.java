package com.rio.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 发送异步消息
 * 异步消通常在对响应时间敏感的应用上,即发送端不能容忍长时间等待 broker 的响应
 */
public class AsyncProducer {
    public static void main(String[] args) throws RemotingException, InterruptedException, MQClientException {
        //1.创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("producer-group1");
        //2.设置 NameServer 的地址
        producer.setNamesrvAddr("localhost:9876");
        //当异步失败设置的重试发送次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.setSendMsgTimeout(1000000);
        //设置消息发送超时时间
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
        for (int i = 0; i < 100000; i++) {
            final int index = i;
            Message message = new Message("Demo1Topic", "TagA", "OrderID" + index, "Hello world".getBytes(Charset.defaultCharset()));
            producer.send(message, new SendCallback() {
                //发送成功的回调函数
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                //发送失败的回调函数
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d EXCEPTION %s %n", index, e);
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            });
        }
        //6.关闭生产者producer
//        Thread.sleep(10000); //异步发送消息时要等消息全部发送完毕才能关闭生产者,
//        producer.shutdown();
    }
}
