package com.utils.demo.google.guava.bloom;

/**
 * @author ldx
 * @date 2022/8/26
 */
public class Test {
    public static void main(String[] args) {
        String str = "h";
        Integer hash = str.hashCode();
        System.out.println("str length=" + str.length() + "\ncode=" + str.hashCode() + "\ncode length=" + hash.toString().length());
    }
}
