package com.myjava.entity;

/**
 * @Author lidexiu
 * @Date 2021/12/28
 * @Description
 */
public class C {
    private A a = new A();
    private B b = new B();

    public void testA(){
        a.hi();
        a.hello();
    }

    public void testB(){
        b.hi();
        b.hello();
    }

    public static void main(String[] args) {
        C c = new C();
        c.testA();
        c.testB();
    }
}
