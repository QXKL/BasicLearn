package com.qx.basicdemo.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试 KitchenByPool 总耗时：48 毫秒
 * 测试 Kitchen 总耗时：25642 毫秒
 * \
 * 线程池的线程数量控制在CPU核心数的2-3倍
 */
public class KitchenByPool {
    public static void main(String[] args) throws InterruptedException {
//        AtomicInteger counter = new AtomicInteger(0);
        long start_time = System.currentTimeMillis();

        // 1. 创建一个有3个厨师的厨师团队（固定大小为3的线程池）
        ExecutorService chefTeam = Executors.newFixedThreadPool(20);

        // 2. 模拟10道菜的任务
        for (int i = 1; i <= 100000; i++) {
            final int dishNum = i; // 任务编号

            // 3. 把任务提交给线程池
            chefTeam.execute(() -> {
//                counter.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + " 正在做第 " + dishNum + " 道菜");
                try {
                    Thread.sleep(1000); // 模拟做菜需要1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 做完了第 " + dishNum + " 道菜");
            });
        }

        // 4. 关闭线程池（不再接受新任务，等现有任务做完就解散）
        chefTeam.shutdown();

        // 主线程在这里卡住，最多等 1 分钟，直到线程池里所有任务真的执行完、线程池彻底关闭
        // 如果所有任务提前做完了，这个方法会立即返回 true，不会傻傻等满 1 分钟
        if (chefTeam.awaitTermination(1, TimeUnit.MINUTES)) {
            long end_time = System.currentTimeMillis();
            System.out.println("===============================");
            System.out.println("KitchenByPool 总耗时：" + (end_time - start_time) + " 毫秒");
        } else {
            System.out.println("等了1小时还没做完，超时了！");
        }
    }
}
