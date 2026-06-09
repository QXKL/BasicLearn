package com.qx.basicdemo.AOP.aop_009_execution_advanced;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExecutionAdvancedRunner implements CommandLineRunner {

    private final InventoryService inventoryService;

    public ExecutionAdvancedRunner(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public void run(String... args) {
        System.out.println("========== 009 execution 进阶示例开始 ==========");

        inventoryService.getStock("SKU-100");
        inventoryService.getStock("SKU-200", 3);
        inventoryService.updateStock("SKU-300", 10);
        inventoryService.deleteStock("SKU-400");
        inventoryService.countAll();

        System.out.println("========== 009 execution 进阶示例结束 ==========");
    }
}
