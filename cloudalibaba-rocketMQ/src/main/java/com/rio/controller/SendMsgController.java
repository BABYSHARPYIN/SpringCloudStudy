package com.rio.controller;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("sendMsg")
public class SendMsgController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/syncSend")
    public SendResult sendMsg(@RequestParam("msg") String msg) {
        return rocketMQTemplate.syncSend("TopicTest:TagA",msg);
    }
}
