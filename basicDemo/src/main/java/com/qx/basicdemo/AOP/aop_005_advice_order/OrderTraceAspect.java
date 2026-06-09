package com.qx.basicdemo.AOP.aop_005_advice_order;

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
public class OrderTraceAspect {

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_005_advice_order.AccountService.*(..))")
    public void accountMethods() {
    }

    @Around("accountMethods()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("1. @Around 前半段 -> " + joinPoint.getSignature().getName());
        try {
            Object result = joinPoint.proceed();
            System.out.println("6. @Around 正常结束 -> " + joinPoint.getSignature().getName());
            return result;
        } finally {
            System.out.println("7. @Around 后半段收尾 -> " + joinPoint.getSignature().getName());
        }
    }

    @Before("accountMethods()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("2. @Before -> " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "accountMethods()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        System.out.println("4. @AfterReturning -> " + joinPoint.getSignature().getName());
        System.out.println("5. 返回值 -> " + result);
    }

    @AfterThrowing(pointcut = "accountMethods()", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
        System.out.println("4. @AfterThrowing -> " + joinPoint.getSignature().getName());
        System.out.println("5. 异常信息 -> " + exception.getMessage());
    }

    @After("accountMethods()")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("6. @After -> " + joinPoint.getSignature().getName());
    }
}
