package com.demo.commons.lambda;

import java.util.function.Consumer;

/**
 * @author ldx
 * @date 2022/7/28
 */
public class Test {
    public static void main(String[] args) {
        Consumer<String>  consumer = System.out::println;
        consumer.accept("hello");
        consumer.accept("world");
    }
}
