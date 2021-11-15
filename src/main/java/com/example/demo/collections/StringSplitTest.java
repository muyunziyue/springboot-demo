package com.example.demo.collections;

/**
 * @Author lidexiu
 * @Date 2021/7/19
 * @Description
 */
public class StringSplitTest {

    public static void SplitTest(){
        String a = "a, b, c,,";
        String[] split = a.split(",");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }

    public static void main(String[] args) {
        SplitTest();
    }
}
