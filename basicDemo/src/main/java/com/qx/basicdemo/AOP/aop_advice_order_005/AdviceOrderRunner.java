package com.qx.basicdemo.AOP.aop_advice_order_005;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdviceOrderRunner implements CommandLineRunner {

    private final AccountService accountService;

    public AdviceOrderRunner(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 005 成功路径开始 ==========");
        String loginResult = accountService.login("alice");
        System.out.println("主流程收到返回值: " + loginResult);

        System.out.println("========== 005 异常路径开始 ==========");
        try {
            accountService.lock("bob");
        } catch (Exception exception) {
            System.out.println("主流程捕获异常: " + exception.getMessage());
        }
        System.out.println("========== 005 示例结束 ==========");
    }
}
