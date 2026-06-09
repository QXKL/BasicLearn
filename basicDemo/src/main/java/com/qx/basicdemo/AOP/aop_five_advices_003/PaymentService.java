package com.qx.basicdemo.AOP.aop_five_advices_003;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String pay(String orderId) {
        System.out.println("执行业务方法: 支付订单 -> " + orderId);
        return "支付成功: " + orderId;
    }

    public void refund(String orderId) {
        System.out.println("执行业务方法: 退款订单 -> " + orderId);
        throw new IllegalStateException("退款失败: 订单状态不允许退款 -> " + orderId);
    }
}
