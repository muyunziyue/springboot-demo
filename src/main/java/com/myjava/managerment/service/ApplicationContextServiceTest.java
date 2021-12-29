package com.myjava.managerment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author lidexiu
 * @Date 2021/12/23
 * @Description
 */
@Service
public class ApplicationContextServiceTest {
    @Autowired
    private ApplicationContext applicationContext;

    public void getBeans(){
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    }
}
