package com.myjava.cloud.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author lidexiu
 */
@Aspect
@Component
public class AnnotationPointcut {

    @Pointcut("execution(* com.myjava.cloud.aop.UserServiceImpl.*(..))")
    public void userPointCut(){}

    @Before("userPointCut()")
    public void before(){
        System.out.println("------方法执行前--------");
    }
    @After("userPointCut()")
    public void after(){
        System.out.println("------方法执行后--------");
    }
    @Around("userPointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕前");
        System.out.println("方法签名：" + joinPoint.getSignature());
        Object proceed = joinPoint.proceed();
        System.out.println("环绕后");
        System.out.println(proceed);
    }
}
