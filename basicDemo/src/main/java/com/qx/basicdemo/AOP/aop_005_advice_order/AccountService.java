package com.qx.basicdemo.AOP.aop_005_advice_order;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public String login(String username) {
        System.out.println("3. 执行业务方法: login -> " + username);
        return "登录成功: " + username;
    }

    public void lock(String username) {
        System.out.println("3. 执行业务方法: lock -> " + username);
        throw new IllegalArgumentException("账号锁定失败: 用户状态异常 -> " + username);
    }
}
