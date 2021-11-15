package com.example.demo.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author lidexiu
 * @Date 2021/7/22
 * @Description
 */
public class StreamReduceTest {

    @Test
    public void reduceTest(){
        List<Integer> numList = Arrays.asList(1,2,3,4,5);
        int result = numList.stream().reduce(Integer::sum).get();//get是optional方法
        System.out.println(result);
    }

    @Test
    public void reduceTest2(){
        List<Integer> numList = Arrays.asList(1,2,3,4,5);
        int result = numList.stream().reduce(100, Integer::sum);
        System.out.println(result);
    }

    @Test
    public void reduceTest3(){
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5, 6);
        ArrayList<String> result = numList.stream().reduce(new ArrayList<String>(), (a, b) -> {
            a.add("element-" + Integer.toString(b));
            return a;
        }, (a, b) -> null);
        System.out.println(result);
    }

    @Test
    public void reduceTest4(){
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5, 6);
        long result = numList.stream().count();
        System.out.println(result);
    }
}
