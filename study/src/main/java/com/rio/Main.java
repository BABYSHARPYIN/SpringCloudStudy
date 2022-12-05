package com.rio;


import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
        /*获取手写的注解配置 bean,该 bean 只有在不是 windows 的环境下才能获取*/
//        IConfig.BeanClass bean = run.getBean(IConfig.BeanClass.class);
//        System.out.println(bean.name);
        RedissonClient bean = run.getBean(RedissonClient.class);
        System.out.println(bean);
        System.out.println("启动成功");
    }

}
