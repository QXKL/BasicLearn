package com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AOPApplication {

    public static void main(String[] args) {
        SpringApplication.run(AOPApplication.class, args);
    }
}
