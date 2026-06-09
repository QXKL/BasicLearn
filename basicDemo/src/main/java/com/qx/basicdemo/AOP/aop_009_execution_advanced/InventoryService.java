package com.qx.basicdemo.AOP.aop_009_execution_advanced;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    public String getStock(String sku) {
        System.out.println("执行业务方法: getStock(String) -> " + sku);
        return "stock:" + sku;
    }

    public String getStock(String sku, int warehouseId) {
        System.out.println("执行业务方法: getStock(String, int) -> " + sku + ", " + warehouseId);
        return "stock:" + sku + "@" + warehouseId;
    }

    public void updateStock(String sku, int count) {
        System.out.println("执行业务方法: updateStock(String, int) -> " + sku + ", " + count);
    }

    public boolean deleteStock(String sku) {
        System.out.println("执行业务方法: deleteStock(String) -> " + sku);
        return true;
    }

    public int countAll() {
        System.out.println("执行业务方法: countAll()");
        return 42;
    }
}
