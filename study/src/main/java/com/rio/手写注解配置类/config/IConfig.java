package com.rio.手写注解配置类.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IConfig {
    public static class BeanClass {
        public String name = "bean";
    }

    @Bean
    @Conditional(IConditional.class)
    public BeanClass beanClass() {
        return new BeanClass();
    }
}
