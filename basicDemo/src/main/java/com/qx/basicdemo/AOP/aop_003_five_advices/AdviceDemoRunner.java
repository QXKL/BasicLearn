package com.qx.basicdemo.AOP.aop_003_five_advices;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdviceDemoRunner implements CommandLineRunner {

    private final PaymentService paymentService;

    public AdviceDemoRunner(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 003 通知示例开始 ==========");

        String payResult = paymentService.pay("ORDER-2001");
        System.out.println("主流程收到返回值: " + payResult);

        try {
            paymentService.refund("ORDER-2002");
        } catch (Exception exception) {
            System.out.println("主流程捕获异常: " + exception.getMessage());
        }

        System.out.println("========== 003 通知示例结束 ==========");
    }
}
