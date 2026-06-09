package com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String send(String receiver) {
        System.out.println("执行业务方法: send -> " + receiver);
        return "发送成功: " + receiver;
    }
}
