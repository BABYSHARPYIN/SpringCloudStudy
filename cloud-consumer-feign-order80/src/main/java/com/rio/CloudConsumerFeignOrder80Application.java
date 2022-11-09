package com.rio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // 开启 feign 支持
public class CloudConsumerFeignOrder80Application {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerFeignOrder80Application.class, args);
        System.out.println("启动成功");

    }

}
