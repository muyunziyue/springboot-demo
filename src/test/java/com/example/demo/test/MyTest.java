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
    @Test
    public void test() throws Exception {
        System.out.println(hexAdd("", -1));
    }

    public String hexAdd(String hexNumber, int offset) throws Exception{
        char c = hexNumber.charAt(0);
        boolean upperCase = Character.isUpperCase(c);
        Long aLong = Long.valueOf(hexNumber, 16);
        aLong += offset;
        return upperCase?Long.toHexString(aLong).toUpperCase(): Long.toHexString(aLong);
    }
}
