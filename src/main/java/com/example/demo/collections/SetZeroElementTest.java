package com.example.demo.collections;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author lidexiu
 * @Date 2021/7/19
 * @Description
 */
public class SetZeroElementTest {

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        Set<String> setb = new HashSet<>();
        set.add("a");
        set.add("b");

        System.out.println(set.containsAll(setb));
        if (true){
            System.out.println("SetZeroElementTest.main");
        }

    }
}
