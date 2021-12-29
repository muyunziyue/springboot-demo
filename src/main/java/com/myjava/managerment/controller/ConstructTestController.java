package com.myjava.managerment.controller;

import com.myjava.entity.User;
import com.myjava.managerment.service.TestManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lidexiu
 * @Date 2021/12/16
 * @Description
 */
@RestController
public class ConstructTestController {

    private final TestManagement testManagement;

    @Autowired
    public ConstructTestController(@Qualifier("defaultTestManagementImpl") TestManagement testManagement){
        this.testManagement = testManagement;
    }

    @PostMapping("/construct/test")
    public User getUser(){
        User user = testManagement.getUser();
        return user;
    }
}
