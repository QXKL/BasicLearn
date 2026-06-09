package com.qx.basicdemo.AOP.aop_006_use_cases;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UseCaseAspect {

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_006_use_cases.ReportService.*(..))")
    public void reportMethods() {
    }

    @Pointcut("execution(* com.qx.basicdemo.AOP.aop_006_use_cases.ReportService.deleteReport(..))")
    public void deleteReportMethod() {
    }

    @Around("reportMethods()")
    public Object logAndMeasure(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("[场景1-日志] 准备执行: " + joinPoint.getSignature().getName());
        System.out.println("[场景1-日志] 参数: " + Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            System.out.println("[场景1-日志] 执行成功: " + joinPoint.getSignature().getName());
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println("[场景2-耗时] " + joinPoint.getSignature().getName() + " 耗时: " + duration + "ms");
        }
    }

    @Before("deleteReportMethod()")
    public void checkPermission(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String operator = String.valueOf(args[0]);
        if (operator.startsWith("guest")) {
            throw new IllegalArgumentException("无权限删除报表: " + operator);
        }
        System.out.println("[场景3-权限] 权限校验通过: " + operator);
    }
}
