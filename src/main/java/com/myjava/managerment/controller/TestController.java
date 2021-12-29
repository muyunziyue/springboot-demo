package com.myjava.managerment.controller;

import com.myjava.entity.HttpResponse;
import com.myjava.entity.User;
import com.myjava.managerment.service.TestManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lidexiu
 * @Date 2021/12/16
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    @Qualifier("testManagementImpl")
    private TestManagement testManagement;

    @GetMapping("/resource")
    public String getResource(@RequestParam String name){
        return name + "=" + testManagement.getSource();
    }

    @PostMapping("/user")
    public User getUser(@RequestBody User user, String name){
        System.out.println(name);
        System.out.println(user.toString());
        return user;
    }

    @PostMapping("/user2")
    public HttpResponse getUser2(){
        User user = testManagement.getUser();
        HttpResponse httpResponse = HttpResponse.create();
        httpResponse.setData(user);
        return httpResponse;
    }

}
