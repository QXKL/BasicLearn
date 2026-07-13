package com.qx.basicdemo.Concurrent;

import lombok.Getter;

public class SynchronizedCounterByChunk {
    @Getter
    private int count = 0;
    private final Object lock = new Object(); // 锁对象

    public void increment() {
        // 对代码块锁
        synchronized (lock) { // 使用lock对象作为锁
            count++;
        }
    }

}
