package com.qx.basicdemo.AOP.aop_pointcut_execution_004;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public String createOrder(String productName) {
        System.out.println("执行业务方法: createOrder -> " + productName);
        return "创建订单成功: " + productName;
    }

    public String queryOrder(String orderId) {
        System.out.println("执行业务方法: queryOrder -> " + orderId);
        return "查询订单成功: " + orderId;
    }

    public void cancelOrder(String orderId) {
        System.out.println("执行业务方法: cancelOrder -> " + orderId);
    }
}
