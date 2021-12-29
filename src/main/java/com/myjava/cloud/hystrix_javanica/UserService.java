package com.myjava.cloud.hystrix_javanica;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
public class UserService {

    @HystrixCommand(fallbackMethod = "fallback")
    public User getUserById(String id){
        throw new RuntimeException("hystrix-javanina-fallback");
    }

    public User fallback(String id, Throwable e){
        System.out.println(e.getClass().getName());
        return new User(id, e.getMessage());
    }
}
