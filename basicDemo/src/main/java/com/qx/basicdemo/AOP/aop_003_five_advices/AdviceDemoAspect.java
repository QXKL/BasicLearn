package com.qx.basicdemo.AOP.aop_003_five_advices;

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
public class AdviceDemoAspect {

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_003_five_advices.PaymentService.*(..))")
    public void paymentMethods() {
    }

    @Before("paymentMethods()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("[@Before] 方法执行前: " + joinPoint.getSignature().getName());
        System.out.println("[@Before] 参数列表: " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("paymentMethods()")
    public void afterAdvice() {
        System.out.println("[@After] 方法结束后: " + "嘻嘻");
    }

    @AfterReturning(pointcut = "paymentMethods()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        System.out.println("[@AfterReturning] 正常返回后: " + joinPoint.getSignature().getName());
        System.out.println("[@AfterReturning] 返回值: " + result);
    }

    @AfterThrowing(pointcut = "paymentMethods()", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
        System.out.println("[@AfterThrowing] 抛异常后: " + joinPoint.getSignature().getName());
        System.out.println("[@AfterThrowing] 异常信息: " + exception.getMessage());
    }

    @Around("paymentMethods()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[@Around] 前半段开始: " + joinPoint.getSignature().getName());
        try {
            Object result = joinPoint.proceed();
            System.out.println("[@Around] 正常结束: " + joinPoint.getSignature().getName());
            return result;
        } finally {
            System.out.println("[@Around] 后半段收尾: " + joinPoint.getSignature().getName());
        }
    }
}
