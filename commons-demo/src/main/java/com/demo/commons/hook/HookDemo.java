package com.demo.commons.hook;

/**
 * @author ldx
 * @date 2022/8/3
 */
public class HookDemo {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("exec hook method");
        }));
        System.out.println("exec the main method");
        while (true) {
            // do noting
        }

    }
}
