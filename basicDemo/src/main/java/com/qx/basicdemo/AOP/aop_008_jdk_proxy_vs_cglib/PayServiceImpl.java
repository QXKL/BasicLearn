package com.qx.basicdemo.AOP.aop_008_jdk_proxy_vs_cglib;

import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {

    @Override
    public String pay(String orderId) {
        System.out.println("执行业务方法: PayServiceImpl.pay -> " + orderId);
        return "支付完成: " + orderId;
    }
}
