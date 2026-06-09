package com.qx.basicdemo.AOP.aop_008_jdk_proxy_vs_cglib;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProxyCompareAspect {

    @Pointcut("execution(* PayService.*(..)) || " +
            "execution(* CouponService.*(..))")
    public void demoMethods() {
    }

    @Before("demoMethods()")
    public void printProxyInfo(JoinPoint joinPoint) {
        System.out.println("[AOP] 当前方法: " + joinPoint.getSignature().getName());
        System.out.println("[AOP] target 类型: " + joinPoint.getTarget().getClass().getName());
        System.out.println("[AOP] proxy 类型: " + joinPoint.getThis().getClass().getName());
    }
}
