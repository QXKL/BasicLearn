package com.qx.basicdemo.Concurrent;

public class MyThread3 extends Thread {
    private final Integer i;

    @Override
    public void run() {
        System.out.println("线程" + i + "正在运行");
    }

    MyThread3(Integer i) {
        this.i = i;
    }
}
