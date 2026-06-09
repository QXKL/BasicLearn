package com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProxyObserveAspect {

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving.MessageService.*(..))")
    public void messageMethods() {
    }

    @Before("messageMethods()")
    public void printJoinPointInfo(JoinPoint joinPoint) {
        System.out.println("[JoinPoint] 当前方法: " + joinPoint.getSignature().getName());
        System.out.println("[JoinPoint] 目标对象类型: " + joinPoint.getTarget().getClass().getName());
        System.out.println("[JoinPoint] 代理对象类型: " + joinPoint.getThis().getClass().getName());
    }

    @Around("messageMethods()")
    public Object observeWeaving(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[Weaving] 增强逻辑开始织入: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println("[Weaving] 增强逻辑结束织入: " + joinPoint.getSignature().getName());
        return result;
    }
}
