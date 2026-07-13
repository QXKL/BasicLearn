package com.qx.basicdemo.Concurrent;

import lombok.Getter;

@Getter
public class Counter {
    private int count = 0;

    public void increment() {
        count++;
    }
}
