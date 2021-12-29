package com.myjava.managerment.service.impl;

import com.myjava.entity.User;
import com.myjava.managerment.service.TestManagement;
import org.springframework.stereotype.Service;

/**
 * @Author lidexiu
 * @Date 2021/12/16
 * @Description
 */
@Service
public class TestManagementImpl implements TestManagement {
    @Override
    public String getSource() {
        return "TestManagementImpl Resource";
    }

    @Override
    public User getUser() {
        return new User("TestManagementImpl", null ,null);
    }
}
