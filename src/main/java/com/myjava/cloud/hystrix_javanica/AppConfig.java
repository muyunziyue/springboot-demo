package com.myjava.cloud.hystrix_javanica;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig{
    @Bean
    public HystrixCommandAspect hystrixAspect(){
        return new HystrixCommandAspect();
    }
    @Bean
    public UserService userService(){
        return new UserService();
    }
}