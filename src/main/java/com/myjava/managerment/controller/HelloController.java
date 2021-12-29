package com.myjava.managerment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lidexiu
 * @Date 2021/12/14
 * @Description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    String hello;

    @GetMapping("/test")
    public String test(){
        return hello;
    }
}
