package com.myjava.reflect.dynamicproxy;

/**
 * @Author lidexiu
 */
public class Host implements Rent {
    @Override
    public void rent() {
        System.out.println("出租房子");
    }
}
