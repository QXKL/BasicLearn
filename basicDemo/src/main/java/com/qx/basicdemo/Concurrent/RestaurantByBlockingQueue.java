package com.qx.basicdemo.Concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RestaurantByBlockingQueue {
    // 创建一个容量为 1 的阻塞队列（相当于传送带上只能放1盘菜）
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>(1); // 可以放字符串对象

    // 厨师做菜
    public void cook(String dish) throws InterruptedException {
        // 如果队列满了，put() 会自动让厨师等待，不需要我们写 wait()
        queue.put(dish);
        System.out.println("厨师：做好了 " + dish + "，放在了出菜口");
    }

    // 服务员上菜
    public void serve() throws InterruptedException {
        // 如果队列是空的，take() 会自动让服务员等待，不需要我们写 wait()
        String dish = queue.take();
        System.out.println("服务员：端走了 " + dish);
    }
}
