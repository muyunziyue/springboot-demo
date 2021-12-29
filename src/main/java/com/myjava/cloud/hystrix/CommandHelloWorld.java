package com.myjava.cloud.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
public class CommandHelloWorld extends HystrixCommand<String> {
    private final String name;
    public CommandHelloWorld(String name){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }
    @Override
    protected String run() throws Exception {
//        return "Hello " + name;
        throw  new HystrixBadRequestException("参数校验异常，用户名称不能为空", new IllegalArgumentException());
    }

    @Override
    protected String getFallback() {
        return "Hello Failure" + name;
    }

    public static void main(String[] args) {
        System.out.println("--------------同步执行");
        CommandHelloWorld joshua = new CommandHelloWorld("Joshua");
        String execute = null;
        try {
            execute = joshua.execute();
        } catch (HystrixBadRequestException e) {
            if (e.getCause().getClass() == IllegalArgumentException.class){
                System.out.println(e.getMessage());
            }
        }
        System.out.println(execute);
        System.out.println("--------------异步执行");

        System.out.println("--------------响应式 阻塞执行");
        System.out.println("--------------响应式 非阻塞执行");
    }
}
