package com.qx.basicdemo.AOP.aop_core_concepts_002;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String register(String username) {
        System.out.println("执行业务方法: 注册用户 -> " + username);
        return "注册成功: " + username;
    }
}
