package com.myjava.reflect;

import java.lang.reflect.Field;

/**
 * @Author lidexiu
 */
public class FieldTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Person person = new Person();
        person.setName("xiaotang");
        getFieldsByReflect(person.getClass(), person);
        System.out.println(person.getName());

    }

    private static void getFieldsByReflect(Class clazz, Object obj) throws NoSuchFieldException, IllegalAccessException {
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        System.out.println(name.getModifiers());
        String s = (String) name.get(obj);
        System.out.println("修改前name值：" + s);
        name.set(obj, "changeByReflect");
    }
}
