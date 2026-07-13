package com.qx.basicdemo.Concurrent;


public class CounterBySync {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
