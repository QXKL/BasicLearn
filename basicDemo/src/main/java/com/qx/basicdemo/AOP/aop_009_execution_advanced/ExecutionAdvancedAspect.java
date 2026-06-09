package com.qx.basicdemo.AOP.aop_009_execution_advanced;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionAdvancedAspect {

    @Pointcut("execution(public * com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(..))")
    public void allPublicMethods() {
    }

    @Pointcut("execution(String com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(..))")
    public void allStringReturnMethods() {
    }

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.get*(..))")
    public void getPrefixMethods() {
    }

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(String))")
    public void oneStringArgMethods() {
    }

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(String, int))")
    public void stringAndIntArgMethods() {
    }

    @Pointcut("execution(boolean com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.delete*(..))")
    public void booleanDeleteMethods() {
    }

    @Before("allPublicMethods()")
    public void matchAllPublicMethods(JoinPoint joinPoint) {
        System.out.println("[匹配 public 方法] " + joinPoint.getSignature().toShortString());
    }

    @Before("allStringReturnMethods()")
    public void matchStringReturnMethods(JoinPoint joinPoint) {
        System.out.println("[匹配返回值 String] " + joinPoint.getSignature().toShortString());
    }

    @Before("getPrefixMethods()")
    public void matchGetPrefixMethods(JoinPoint joinPoint) {
        System.out.println("[匹配方法名前缀 get*] " + joinPoint.getSignature().toShortString());
    }

    @Before("oneStringArgMethods()")
    public void matchOneStringArgMethods(JoinPoint joinPoint) {
        System.out.println("[匹配参数 (String)] " + joinPoint.getSignature().toShortString());
    }

    @Before("stringAndIntArgMethods()")
    public void matchStringAndIntArgMethods(JoinPoint joinPoint) {
        System.out.println("[匹配参数 (String, int)] " + joinPoint.getSignature().toShortString());
    }

    @Before("booleanDeleteMethods()")
    public void matchBooleanDeleteMethods(JoinPoint joinPoint) {
        System.out.println("[匹配 boolean + delete*] " + joinPoint.getSignature().toShortString());
    }
}
