package com.qx.basicdemo.AOP.aop_008_jdk_proxy_vs_cglib;

import org.springframework.stereotype.Service;

@Service
public class CouponService {

    public String sendCoupon(String username) {
        System.out.println("执行业务方法: CouponService.sendCoupon -> " + username);
        return "发券成功: " + username;
    }
}
