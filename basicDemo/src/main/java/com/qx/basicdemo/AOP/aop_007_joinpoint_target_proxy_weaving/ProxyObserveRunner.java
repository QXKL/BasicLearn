package com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProxyObserveRunner implements CommandLineRunner {

    private final MessageService messageService;

    public ProxyObserveRunner(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========== 007 概念示例开始 ==========");
        System.out.println("注入到 Runner 中的 bean 类型: " + messageService.getClass().getName());
        System.out.println("是否为 AOP 代理: " + AopUtils.isAopProxy(messageService));
        System.out.println("目标类类型: " + AopUtils.getTargetClass(messageService).getName());

        if (messageService instanceof Advised advised) {
            Object targetObject = advised.getTargetSource().getTarget();
            if (targetObject != null) System.out.println("从代理中取出的目标对象类型: " + targetObject.getClass().getName());
        }

        String result = messageService.send("tom@example.com");
        System.out.println("主流程收到返回值: " + result);
        System.out.println("========== 007 概念示例结束 ==========");
    }
}
