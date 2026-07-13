package com.qx.basicdemo.Concurrent;

public class MyThread2 extends Thread {
    private int number; // 我们要传递的参数

    // 通过构造函数传递参数
    MyThread2(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println("线程处理数字: " + number);
        // 对数字做一些操作
        System.out.println("数字加1后: " + (number + 1));
    }
}

