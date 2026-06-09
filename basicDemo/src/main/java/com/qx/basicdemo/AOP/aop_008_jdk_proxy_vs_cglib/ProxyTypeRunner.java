package com.qx.basicdemo.AOP.aop_008_jdk_proxy_vs_cglib;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProxyTypeRunner implements CommandLineRunner {

    private final PayService payService;
    private final CouponService couponService;

    public ProxyTypeRunner(PayService payService, CouponService couponService) {
        this.payService = payService;
        this.couponService = couponService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 008 代理类型示例开始 ==========");

        System.out.println("[接口服务] bean 类型: " + payService.getClass().getName());
        System.out.println("[接口服务] 是否 JDK 代理: " + AopUtils.isJdkDynamicProxy(payService));
        System.out.println("[接口服务] 是否 CGLIB 代理: " + AopUtils.isCglibProxy(payService));
        String payResult = payService.pay("ORDER-8001");
        System.out.println("[接口服务] 返回值: " + payResult);

        System.out.println("[普通类服务] bean 类型: " + couponService.getClass().getName());
        System.out.println("[普通类服务] 是否 JDK 代理: " + AopUtils.isJdkDynamicProxy(couponService));
        System.out.println("[普通类服务] 是否 CGLIB 代理: " + AopUtils.isCglibProxy(couponService));
        String couponResult = couponService.sendCoupon("alice");
        System.out.println("[普通类服务] 返回值: " + couponResult);

        System.out.println("========== 008 代理类型示例结束 ==========");
    }
}
