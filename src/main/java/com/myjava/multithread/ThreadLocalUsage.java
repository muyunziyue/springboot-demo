package com.myjava.multithread;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author lidexiu
 * @Date 2021/11/17
 * @Description
 */
@RestController
@RequestMapping(value = "/threadlocal")
public class ThreadLocalUsage {
    private static ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    static void print(String str) {
        System.out.println(str + ":" + currentUser.get());
    }

    @GetMapping("/wrong")
    public Map wrong(@RequestParam("userId") Integer userId) {
        //设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        //设置用户信息到ThreadLocal
        currentUser.set(userId);
        //设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        //汇总输出两次查询结果
        Map result = new HashMap();

        result.put("before", before);
        result.put("after", after);
        return result;
    }


}
