package com.qx.basicdemo.AOP.aop_008_jdk_proxy_vs_cglib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class JdkProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdkProxyApplication.class, args);
    }
}
