package com.qx.basicdemo.Concurrent;

public class BasicThread extends Thread {
    @Override
    public void run() {
        System.out.println("我的线程正在运行");
    }
}

