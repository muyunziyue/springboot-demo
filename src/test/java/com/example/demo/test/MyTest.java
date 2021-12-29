package com.example.demo.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * @Author lidexiu
 * @Date 2021/12/23
 * @Description
 */
public class MyTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void getBeans(){
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    }
}
