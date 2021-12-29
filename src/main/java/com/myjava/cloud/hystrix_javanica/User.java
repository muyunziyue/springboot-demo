package com.myjava.cloud.hystrix_javanica;

import lombok.Data;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
@Data
public class User {
    private String id;
    private String name;
    private String profile;

    public User(){
        System.out.println("construct new user");
    }
    public User(String id, String name){
        this.id = id;
        this.name = name;
        System.out.println(this.toString());
    }
}
