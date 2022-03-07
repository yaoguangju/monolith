package com.mochen.thread.example.pool;

import cn.hutool.core.date.DateUtil;

import java.util.concurrent.*;

public class Test1 {

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(2,
                10,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(5, true),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> {
                // 获取线程名称,默认格式:pool-1-thread-1
                System.out.println(DateUtil.now() + " " + Thread.currentThread().getName() + " " + index);
                // 等待2秒
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
