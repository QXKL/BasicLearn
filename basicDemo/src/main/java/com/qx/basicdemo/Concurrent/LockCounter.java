package com.qx.basicdemo.Concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ① 锁可以“尝试获取”，拿不到就撤（避免死锁）
 * 用 synchronized 时，如果线程拿不到锁，就会死等（进入阻塞状态），一直等下去。
 * 而 ReentrantLock 提供了 tryLock() 方法：
 * \
 * “哥们，这把锁我现在能拿吗？能拿我就拿，不能拿（比如别人正占着）那我先去干别的事，不等了。”
 * \
 * ② 锁可以被“中断”
 * 如果一个线程用 synchronized 在死等一把锁，你拿它没有任何办法，只能干看着。
 * 而 ReentrantLock 提供了 lockInterruptibly()：
 * \
 * 如果一个线程等锁等得太久了，主线程可以给它发送一个中断信号（thread.interrupt()），告诉它：“别等了，取消任务吧。” 线程就会醒过来，放弃等锁。
 * \
 * ③ 支持“公平锁”
 * synchronized 是非公平锁。意思是锁释放后，大家一起抢，谁抢到算谁的，有些运气差的线程可能一辈子抢不到（线程饥饿）。
 * \
 * ReentrantLock 在创建时可以传入一个参数 new ReentrantLock(true) 开启公平锁。这样线程就会乖乖排队，先来后到，绝对公平。
 * n\
 * ④ 锁可以绑定多个“条件变量”（Condition）
 * synchronized 配合 wait() 和 notify() 只能唤醒“任意一个”或者“全部”等待的线程。
 * ReentrantLock 可以创建多个 Condition，实现精准唤醒某一部分线程（比如精准唤醒“生产者”或“消费者”）。
 */
public class LockCounter {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}
