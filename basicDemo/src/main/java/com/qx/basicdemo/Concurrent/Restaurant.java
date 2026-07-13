package com.qx.basicdemo.Concurrent;

/**
 * wait()：让当前线程等待，并释放锁。直到另一个线程叫醒它。
 * notify()：随机叫醒一个正在等待的线程。
 * notifyAll()：叫醒所有正在等待的线程。
 */
public class Restaurant {
    private String dish = null;

    public synchronized void cook(String dish) throws InterruptedException {
        while (this.dish!=null) {
            System.out.println("菜还在，等会");
            wait();
        }

        System.out.println("厨师做菜: " + dish);
        this.dish = dish;

        notify();   // 随机唤醒等待的线程
    }

    public synchronized void serve() throws InterruptedException {
        while (this.dish==null) {
            System.out.println("菜还没好，等会");
            wait();
        }

        System.out.println("服务员上菜");
        this.dish = null;

        notify();   // 随机唤醒等待的线程
    }
}
