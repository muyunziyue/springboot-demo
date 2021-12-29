package com.myjava.annotation;

import lombok.Data;

/**
 * @Author lidexiu
 * @Date 2021/12/1
 * @Description
 */
@Data
public class Person {
    @Range(min = 1, max = 20)
    public String name;
    @Range(max = 10)
    public Integer age;

}
