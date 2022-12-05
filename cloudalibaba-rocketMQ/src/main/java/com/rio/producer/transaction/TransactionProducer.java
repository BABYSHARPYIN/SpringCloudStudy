package com.rio.producer.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;

public class TransactionProducer {
    public static void main(String[] args) throws MQClientException {
        //创建消息的生产者
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("producer-group-transaction");
        // 指定 nameServer
        transactionMQProducer.setNamesrvAddr("127.0.0.1:9876");
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            /**
             * 在此方法中执行本地事务
             * 发送半事务消息 执行的事务方法
             * */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                System.out.println("发送消息时间: "+new Date().toLocaleString());
                //情况 1 本地事务执行成功
                LocalTransactionState commitMessage = LocalTransactionState.COMMIT_MESSAGE;

                //情况 2 本地事务执行失败 进行 rollback
                LocalTransactionState rollbackMessage = LocalTransactionState.ROLLBACK_MESSAGE;

                //情况 3 本地事务执行中 不确定是成功还是失败
                LocalTransactionState unknow = LocalTransactionState.UNKNOW;

                return commitMessage;
            }

            /**
             * 检查本地事务,事务状态回查
             * MQServer 会在一段时间之后进行 check back
             * */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                //事务回查,MQServer在没有收到Producer的 commit或 rollback时就会调用这个方法
                System.out.println("事务回查时间: " + new Date().toLocaleString());
                System.out.println("消息的 tag: "+messageExt.getTags());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        transactionMQProducer.start();
        Message message = new Message("TransactionTopic", "TagA","Hello Transaction".getBytes());
        //第二个参数表示针对哪些消息使用事务的方式发送,传null 表示不控制全部以事务发送
        TransactionSendResult transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, null);
        System.out.println("发送状态: " + transactionSendResult.getSendStatus());

        //不能关闭生产者,否则监听器将没有机会触发
    }
}
