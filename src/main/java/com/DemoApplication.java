package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
//@EnableCircuitBreaker
//@ImportResource(locations = {"classpath:aop-test.xml"})
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
    }

}
