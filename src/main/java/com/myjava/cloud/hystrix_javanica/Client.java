package com.myjava.cloud.hystrix_javanica;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
public class Client {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        //同步调用
        System.out.println(userService.getUserById("joshua"));
        //异步调用

    }
}
