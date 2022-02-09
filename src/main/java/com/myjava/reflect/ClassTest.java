package com.myjava.reflect;

import com.myjava.reflect.dynamicproxy.Rent;

/**
 * @Author lidexiu
 */
public class ClassTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?>[] interfaces = Rent.class.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface.getName());
        }
    }
}
