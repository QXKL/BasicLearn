package com.qx.basicdemo.Concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Map<Integer, Runnable> taskMap = new HashMap<>();
    private static final boolean is_loop = false;

    public static void main(String[] args) throws Exception {
        if (is_loop) {
            runTaskLoop();  // 循环
        } else {
            Integer select = 6;
            runTask(select);    // 固定，一次性
        }
    }

    static {
        put(1, Func::func1);
        put(2, Func::func2);
        put(3, Func::func3);
        put(4, Func::func4);
        put(5, Func::func5);
        put(6, Func::func6);
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
}

class Func {
    private static final Logger log = LoggerFactory.getLogger(Func.class);

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

        for (int i = 0; i < 1000; i++) {
            new Thread(counter::increment).start();
        }

        temp500s();

        System.out.println("最终计数：" + counter.getCount());
    }

    private static void tempTime(int ms) {
        try {
            Thread.sleep(ms);  // 等待time毫秒, 确保所有的线程运行完成
        } catch (InterruptedException e) {
            log.atInfo().log("线程被中断");
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