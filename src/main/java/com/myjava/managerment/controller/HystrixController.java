package com.myjava.managerment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
@RestController
public class HystrixController {

    @PostMapping("/hello")
    public String hello(String name){
        return "Hello " + name;
    }
}
