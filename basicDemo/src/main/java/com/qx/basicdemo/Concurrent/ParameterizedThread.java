package com.qx.basicdemo.Concurrent;

public class ParameterizedThread extends Thread {
    private final int number; // 我们要传递的参数

    // 通过构造函数传递参数
    ParameterizedThread(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println("线程处理数字: " + number);
        // 对数字做一些操作
        System.out.println("数字加1后: " + (number + 1));
    }
}

