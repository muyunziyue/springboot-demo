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
public class DefaultTestManagementImpl implements TestManagement {
    @Override
    public String getSource() {
        return "DefaultTestManagementImpl Resource";
    }


    @Override
    public User getUser() {
        return new User("DefaultTestManagementImpl", 18, "北京");
    }
}
