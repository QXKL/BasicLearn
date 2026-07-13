package com.qx.basicdemo.Concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Kitchen {
    public static void main(String[] args) throws InterruptedException {
//        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(100000);

        long start_time = System.currentTimeMillis();

        // 2. 模拟10道菜的任务
        for (int i = 1; i <= 100000; i++) {
            final int dishNum = i; // 任务编号

            // 3. 把任务提交给线程池
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " 正在做第 " + dishNum + " 道菜");
                try {
//                    counter.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
                System.out.println(Thread.currentThread().getName() + " 做完了第 " + dishNum + " 道菜");
            }).start();
        }

        countDownLatch.await();

        long end_time = System.currentTimeMillis();
        System.out.println("Kitchen 总耗时：" + (end_time - start_time) + " 毫秒");
    }
}
