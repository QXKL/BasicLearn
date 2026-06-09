package com.qx.basicdemo.AOP.aop_000_quick_start;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_000_quick_start.*.*(..))")
    public void orderServiceMethods() {
    }

    @Before("orderServiceMethods()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("[Before] 准备执行方法: " + joinPoint.getSignature().getName());
        System.out.println("[Before] 方法参数: " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("orderServiceMethods()")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("[After] 方法执行结束: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "orderServiceMethods()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        System.out.println("[AfterReturning] 方法返回值: " + result);
        System.out.println("[AfterReturning] 成功方法: " + joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "orderServiceMethods()", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
        System.out.println("[AfterThrowing] 异常方法: " + joinPoint.getSignature().getName());
        System.out.println("[AfterThrowing] 异常信息: " + exception.getMessage());
    }

    @Around("orderServiceMethods()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("[Around] 环绕开始: " + joinPoint.getSignature().getName());
        try {
            Object result = joinPoint.proceed();
            System.out.println("[Around] 环绕成功: " + joinPoint.getSignature().getName());
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println("[Around] 环绕结束, 耗时 = " + duration + "ms");
        }
    }
}
