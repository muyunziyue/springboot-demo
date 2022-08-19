package com.demo.base.reflect;

import java.lang.reflect.Field;

/**
 * @author ldx
 * @date 2022/8/4
 */
public class ClassDemo {

    public String name = "hello";
    private Integer age = 10;

    public void testMethod(){
        System.out.println("testMethod");
    }

    public ClassDemo(){
        System.out.println("classDemo 构造函数");
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
//        Class<ClassDemo> classDemoClass = ClassDemo.class;
//        ClassDemo classDemo = new ClassDemo();
//        Class<? extends ClassDemo> classDemoClass1 = classDemo.getClass();

        Class<?> clazz = Class.forName("com.demo.base.reflect.ClassDemo");
        ClassDemo classDemo1 = (ClassDemo) clazz.newInstance();
        classDemo1.testMethod();

        Field name = clazz.getDeclaredField("age");
        name.setAccessible(true);
        System.out.println(name.getName());
        System.out.println(name.getType());
        name.set(classDemo1, 13232);
        System.out.println(name.get(classDemo1));
    }
}
