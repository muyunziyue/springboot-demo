package com.myjava.entity;

/**
 * @Author lidexiu
 * @Date 2021/12/28
 * @Description
 */
public class B extends A{
    @Override
    protected void hello() {
        super.hello();
    }

    @Override
    public void hi() {
        super.hi();
    }

    public void hiB(){
        String aaa = this.aaa;
    }

}
