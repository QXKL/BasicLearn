package com.qx.basicdemo.Concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Map<Integer, Runnable> taskMap = new HashMap<>();
    private static final boolean is_loop = false;

    public static void main(String[] args) throws Exception {
        // 优化：利用反射自动扫描注册
        registerTask();

        if (is_loop) {
            runTaskLoop();  // 循环
        } else {
            Integer select = 1;
            runTask(select);    // 固定，一次性
        }
    }

    private static void put(Integer sel, Runnable runnable) {
        if (taskMap.containsKey(sel)) {
            System.out.println("任务已存在");
            return;
        }
        taskMap.put(sel, runnable);

        // taskMap.putIfAbsent(sel, runnable); // 等效上边的
    }

    private static void runTask(Integer select) {
        Runnable task = taskMap.get(select);
        if (task != null) {
            task.run();
        } else {
            System.out.println("未知的测试编号");
        }
    }

    private static void runTaskLoop() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入数字 (输入 -1 退出): ");

        while (true) {
            // 3. 判断用户输入的是不是整数，防止胡乱输入导致程序崩溃
            if (scanner.hasNextInt()) {
                int select = scanner.nextInt();

                // 顺手加个退出机制，不然死循环出不去了
                if (select == -1) {
                    System.out.println("程序退出。");
                    break;
                }

                System.out.println("==================");
                runTask(select);
            } else {
                // 如果用户输入的不是数字（比如输了字母 abc）
                System.out.println("请输入合法的整数");
                scanner.next(); // 极其重要：吃掉错误的输入，防止死循环
            }
        }

        scanner.close(); // 循环结束后关闭资源
    }

    static void registerTask() {
        try {
            // 1. 获取 Func 类中定义的所有方法
            Method[] methods = Func.class.getDeclaredMethods();

            for (Method method : methods) {
                String name = method.getName();

                // 2. 筛选出名字以 "func" 开头的方法
                if (name.startsWith("func")) {
                    // 3. 提取方法名后面的数字作为 key（例如 "func6" 提取出 6）
                    String numStr = name.substring(4);
                    if (!numStr.isEmpty()) {
                        int sel = Integer.parseInt(numStr);

                        // 4. 将其包装为 Runnable 并注册
                        // 由于是 static 方法，method.invoke(null) 即可执行
                        put(sel, () -> {
                            try {
                                method.setAccessible(true); // 确保私有/包访问权限方法也能调
                                method.invoke(null);
                            } catch (Exception e) {
                                logger.info("【系统提示】方法调用失败", e);
                            }
                        });
                    }
                }
            }
            System.out.println("【系统提示】已自动注册 " + taskMap.size() + " 个测试任务。");
        } catch (Exception e) {
            System.err.println("自动注册任务失败：" + e.getMessage());
        }

    }
}

class Func {
    private static final Logger logger = LoggerFactory.getLogger(Func.class);

    static void func1() {
        BasicThread thread = new BasicThread();
        thread.start(); // 启动线程
        System.out.println("主线程也在运行");
    }

    static void func2() {
        // 创建线程并传递参数
        ParameterizedThread thread2 = new ParameterizedThread(5);
        thread2.start();
    }

    static void func3() {
        // 创建5个线程，每个线程处理不同的数字
        for (int i = 0; i < 5; i++) {
            ParameterizedThread thread3 = new ParameterizedThread(i);
            thread3.start();
        }
        System.out.println("主线程结束");
    }

    static void func4() {
        // 创建5个线程
        for (int i = 0; i < 5; i++) {
            ImmutableParamThread thread = new ImmutableParamThread(i);
            thread.start();
        }

        // 等待所有线程完成
        temp500s();

        System.out.println("所有线程都已完成");
    }

    // 竞态
    static void func5() {
        UnsynchronizedCounter unsynchronizedCounter = new UnsynchronizedCounter();

        for (int i = 0; i < 1000; i++) {
            new Thread(unsynchronizedCounter::increment).start();
        }

        temp500s();

        System.out.println("最终计数：" + unsynchronizedCounter.getCount());
    }

    // 关键字
    static void func6() {
        SynchronizedCounter counter = new SynchronizedCounter();

        int threadCount = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount); // 创建一个门闩，初始计数为线程数量

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    counter.increment();
                } finally {
                    latch.countDown(); // 每个线程完成时，减少门闩计数
                }
            }).start();
        }

        try {
            latch.await(); // 等待所有线程完成
        } catch (InterruptedException e) {
            logger.atInfo().log("所有线程都已完成");
        }

        System.out.println("最终计数：" + counter.getCount());
    }

    static void func7() {
        SynchronizedCounterByChunk synchronizedCounterByChunk = new SynchronizedCounterByChunk();

        synchronizedCounterByChunk.increment();

        System.out.println("最终计数：" + synchronizedCounterByChunk.getCount());
    }

    static void func8() {

    }

    static void func9() {

    }

    static void func10() {

    }

    static void func11() {

    }

    private static void tempTime(int ms) {
        try {
            Thread.sleep(ms);  // 等待time毫秒, 确保所有的线程运行完成
        } catch (InterruptedException e) {
            logger.atInfo().log("线程被中断");
        }
    }

    private static void temp500s() {
        tempTime(500);
    }

    private static void temp1000s() {
        tempTime(1000);
    }

    private static void temp3000s() {
        tempTime(3000);
    }
}