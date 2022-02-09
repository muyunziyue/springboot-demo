package com.todo;


import java.text.ParseException;

/**
 * @Author lidexiu
 * @Date 2021/12/28
 * @Description
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        Integer a = 1;
        Integer b = 2;
        System.out.println("a=" + a + ",b="+b);
        change(a, b);
        System.out.println("a=" + a + ",b="+b);
    }

    private static void change(Integer a, Integer b) {
        a = 3;
        b =4;
        System.out.println("a=" + a + ",b="+b);
    }
}
