package com.myjava.cloud.aop;

import org.springframework.stereotype.Service;

/**
 * @Author lidexiu
 */
@Service("userService")
public class UserServiceImpl implements UserService{
    @Override
    public void add() {
        System.out.println("新添用户");
    }

    @Override
    public void delete() {
        System.out.println("删除用户");
    }

    @Override
    public void update() {
        System.out.println("更新用户");
    }

    @Override
    public void search() {
        System.out.println("查找用户");
    }
}
