package com.qx.basicdemo.Concurrent;

import lombok.Getter;

@Getter
public class UnsynchronizedCounter {
    private int count = 0;

    public void increment() {
        count++;
    }
}
