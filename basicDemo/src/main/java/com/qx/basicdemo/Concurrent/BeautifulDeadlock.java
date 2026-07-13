package com.qx.basicdemo.Concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1 按照固定的顺序获取锁
 * 2 使用超时机制（通过 ReentrantLock.tryLock 实现）
 * 3 尽量减少锁的持有时间（只在绝对需要临界区资源时加锁，执行完立马释放）
 */
public class BeautifulDeadlock {
    // 替换为 ReentrantLock 以便支持超时机制
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(100);  // 原则3：把不属于临界区、耗时的操作（如 Thread.sleep）移到锁的外面！
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                // 原则2：尝试获取 lock1，最多等 500 毫秒
                if (lock1.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("线程1: 获取了lock1");

                        // Thread.sleep(100);
                        // 错误示范：过去把 sleep 放在锁内，白白霸占着锁，增加了死锁和阻塞风险。

                        System.out.println("线程1: 准备尝试获取lock2");

                        // 原则1 & 2：按照固定顺序，尝试获取 lock2
                        if (lock2.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("线程1: 获取了lock2，成功执行核心业务");
                            } finally {
                                lock2.unlock();
                            }
                        } else {
                            System.out.println("线程1: 获取lock2超时，放弃本次操作");
                        }

                    } finally {
                        lock1.unlock(); // 原则3：执行完业务立马在 finally 里释放锁，减少持有时间
                    }
                } else {
                    System.out.println("线程1: 获取lock1超时，直接撤退");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                // 原则1：依然保持固定顺序，先拿 lock1，再拿 lock2
                if (lock1.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("线程2: 获取了lock1");
                        System.out.println("线程2: 准备尝试获取lock2");

                        if (lock2.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("线程2: 获取了lock2，成功执行核心业务");
                            } finally {
                                lock2.unlock();
                            }
                        } else {
                            System.out.println("线程2: 获取lock2超时，放弃并释放已持有的lock1");
                        }
                    } finally {
                        lock1.unlock();
                    }
                } else {
                    System.out.println("线程2: 获取lock1超时");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
    }
}

