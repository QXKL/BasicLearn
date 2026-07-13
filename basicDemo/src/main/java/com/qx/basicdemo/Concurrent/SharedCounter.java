package com.qx.basicdemo.Concurrent;

public class SharedCounter {
    private static int counter = 0;

    public static void main(String[] args) {
        // 创建10个线程，每个线程都增加计数器
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter++; // 每个线程都增加计数器
                }
                System.out.println("线程完成，计数器值: " + counter);
            }).start();
        }

        try {
            Thread.sleep(1000); // 等待所有线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("最终计数器值: " + counter);
    }
}
