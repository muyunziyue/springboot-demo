package com.todo;

import com.myjava.entity.Outer;

/**
 * @Author lidexiu
 * @Date 2021/12/28
 * @Description
 */
public class Test {
    public static void main(String[] args) {
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.hello();
    }
}
