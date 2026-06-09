package com.qx.basicdemo.AOP.aop_pointcut_execution_004;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExecutionDemoRunner implements CommandLineRunner {

    private final OrderService orderService;

    public ExecutionDemoRunner(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 004 execution 示例开始 ==========");

        orderService.createOrder("Java 并发编程");
        System.out.println("----------");
        orderService.queryOrder("ORDER-3001");
        System.out.println("----------");
        orderService.cancelOrder("ORDER-3002");

        System.out.println("========== 004 execution 示例结束 ==========");
    }
}
