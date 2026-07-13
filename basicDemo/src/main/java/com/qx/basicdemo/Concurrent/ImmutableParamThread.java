package com.qx.basicdemo.Concurrent;

public class ImmutableParamThread extends Thread {
    private final Integer i;

    @Override
    public void run() {
        System.out.println("线程" + i + "正在运行");
    }

    ImmutableParamThread(Integer i) {
        this.i = i;
    }
}
