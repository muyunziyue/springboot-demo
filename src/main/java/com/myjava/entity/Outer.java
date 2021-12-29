package com.myjava.entity;

/**
 * @Author lidexiu
 * @Date 2021/12/28
 * @Description
 */
public class Outer {
    private String outerField;

    public void getInnerField(){
//        System.out.println();
    }

    public class Inner{
        private String innerField;
        public void hello(){
            outerField = "inner init outer field";
            System.out.println("inner hello: " + outerField);

        }
    }

    public static void main(String[] args) {
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.hello();
    }
}
