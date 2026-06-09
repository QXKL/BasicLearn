package com.qx.basicdemo.AOP.aop_002_core_concepts;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConceptDemoRunner implements CommandLineRunner {

    private final UserService userService;

    public ConceptDemoRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 002 概念示例开始 ==========");
        String result = userService.register("tom");
        System.out.println("主流程收到返回值: " + result);
        System.out.println("========== 002 概念示例结束 ==========");
    }
}
