package com.qx.basicdemo.AOP.aop_002_core_concepts;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConceptAspect {

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_002_core_concepts.UserService.*(..))")
    public void userMethods() {
    }

    @Before("userMethods()")
    public void printMethodName(JoinPoint joinPoint) {
        System.out.println("[Advice] 方法执行前打印方法名: " + joinPoint.getSignature().getName());
    }
}
