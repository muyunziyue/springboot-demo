package com.myjava.entity;

/**
 * @Author lidexiu
 * @Date 2021/12/28
 * @Description
 */
public class A {
    protected String aaa;
    public String bbb;

    protected void hello(){
        System.out.println("hello");
    }

    public void hi(){
        hello();
    }

    public static void main(String[] args) {
        A a = new A();
        a.hi();
    }
}
