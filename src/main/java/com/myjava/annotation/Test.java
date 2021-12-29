package com.myjava.annotation;

import java.lang.reflect.Field;

/**
 * @Author lidexiu
 * @Date 2021/12/1
 * @Description
 */
public class Test {
    public static void main(String[] args) throws IllegalAccessException {
        Person person = new Person();
        person.setAge(18);
        person.setName("xiaowang");
        check(person);

    }

    public static void check(Person person) throws IllegalAccessException {
        Field[] fields = Person.class.getFields();
        for (Field field :
                fields) {
            Range range = field.getAnnotation(Range.class);
            if (range != null) {
                Object o = field.get(person);
                if (o instanceof String) {
                    String o1 = (String) o;
                    if (o1.length() < range.min() || o1.length() > range.max()) {
                        throw new IllegalArgumentException("Invalid field " + field.getName());
                    }
                }
            }
        }
    }
}
