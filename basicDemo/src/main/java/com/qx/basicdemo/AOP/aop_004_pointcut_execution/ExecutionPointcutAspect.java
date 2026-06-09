package com.qx.basicdemo.AOP.aop_004_pointcut_execution;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionPointcutAspect {

    // 拦截所有
    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_004_pointcut_execution.OrderService.*(..))")
    public void allOrderMethods() {
    }

    // 拦截所有create开头的方法
    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_004_pointcut_execution.OrderService.create*(..))")
    public void createMethods() {
    }

    // 拦截指定方法
    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_004_pointcut_execution.OrderService.queryOrder(..))")
    public void queryOrderMethod() {
    }

    @Before("allOrderMethods()")
    public void matchAllMethods(JoinPoint joinPoint) {
        System.out.println("[匹配全部方法] " + joinPoint.getSignature().getName());
    }

    @Before("createMethods()")
    public void matchCreateMethods(JoinPoint joinPoint) {
        System.out.println("[只匹配 create*] " + joinPoint.getSignature().getName());
    }

    @Before("queryOrderMethod()")
    public void matchQueryMethod(JoinPoint joinPoint) {
        System.out.println("[只匹配 queryOrder] " + joinPoint.getSignature().getName());
    }
}
