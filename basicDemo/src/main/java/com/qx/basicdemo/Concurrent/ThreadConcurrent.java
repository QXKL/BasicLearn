package com.qx.basicdemo.Concurrent;

public class ThreadConcurrent extends Thread {
    private int counter;
    private static int ThreadCounter = 0;

    ThreadConcurrent(int counter) {
        this.counter = counter;
        ThreadCounter++;
    }

    @Override
    public void run() {
        System.out.println("ThreadConcurrent" + ThreadCounter + "counter:" + plus_1(counter));
    }

    public int plus_1(int counter) {
        return counter++;
    }
}

