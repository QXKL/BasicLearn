package com.qx.basicdemo.Concurrent;

/**
 * 1 按照固定的顺序获取锁(如下)
 * 2 使用超时机制
 * 3 尽量减少锁的持有时间
 */
public class NoDeadlockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("线程1: 获取了lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1: 等待lock2");
                synchronized (lock2) {
                    System.out.println("线程1: 获取了lock2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock1) { // 改为先获取lock1
                System.out.println("线程2: 获取了lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2: 等待lock2");
                synchronized (lock2) {
                    System.out.println("线程2: 获取了lock2");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
