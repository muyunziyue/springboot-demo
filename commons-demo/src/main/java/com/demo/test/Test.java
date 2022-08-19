package com.demo.test;

/**
 * @author ldx
 * @date 2022/8/19
 */
public class Test {

    InterfaceTest test(){
        return () -> {
            return "heelo";
        }
        ;
    }
}
