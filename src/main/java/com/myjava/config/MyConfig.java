package com.myjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lidexiu
 * @Date 2021/12/14
 * @Description
 */
@Configuration
public class MyConfig {

    @Bean
    public String hello(){
        return "init by myConfig";
    }
}
