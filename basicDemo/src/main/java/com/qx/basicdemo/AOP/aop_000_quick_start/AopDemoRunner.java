package com.qx.basicdemo.AOP.aop_000_quick_start;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AopDemoRunner implements CommandLineRunner {

    private final OrderService orderService;

    public AopDemoRunner(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== AOP 学习示例开始 ==========");
        String result = orderService.createOrder("Java 入门书");
        System.out.println("主流程收到返回值: " + result);

        try {
            orderService.cancelOrder("ORDER-1001");
        } catch (Exception exception) {
            System.out.println("主流程捕获异常: " + exception.getMessage());
        }
        System.out.println("========== AOP 学习示例结束 ==========");
    }
}
