package com.qx.basicdemo.AOP.aop_000_quick_start;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public String createOrder(String productName) {
        System.out.println("执行业务方法: 创建订单, 商品 = " + productName);
        return "订单创建成功: " + productName;
    }

    public void cancelOrder(String orderId) {
        System.out.println("执行业务方法: 取消订单, 订单号 = " + orderId);
        throw new IllegalArgumentException("模拟异常: 订单不存在, orderId = " + orderId);
    }
}
