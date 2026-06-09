package com.qx.basicdemo.AOP.aop_006_use_cases;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UseCaseRunner implements CommandLineRunner {

    private final ReportService reportService;

    public UseCaseRunner(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 006 场景示例开始 ==========");

        String result = reportService.generateReport("sales-2026");
        System.out.println("主流程收到返回值: " + result);

        reportService.deleteReport("admin_zhang", "sales-2026");

        try {
            reportService.deleteReport("guest_li", "audit-2026");
        } catch (Exception exception) {
            System.out.println("主流程捕获异常: " + exception.getMessage());
        }

        System.out.println("========== 006 场景示例结束 ==========");
    }
}
