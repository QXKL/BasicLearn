package com.qx.basicdemo.AOP.aop_006_use_cases;

import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public String generateReport(String reportName) {
        System.out.println("执行业务方法: generateReport -> " + reportName);
        return "报表生成成功: " + reportName;
    }

    public void deleteReport(String operator, String reportName) {
        System.out.println("执行业务方法: deleteReport -> " + reportName);
        System.out.println("当前操作人: " + operator);
    }
}
