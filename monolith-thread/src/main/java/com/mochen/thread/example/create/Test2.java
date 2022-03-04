package com.mochen.thread.example.create;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test2 {
    // 使用 Runnable 配合 Thread
    public static void main(String[] args) {
        // 创建任务对象
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                log.debug("hello");
            }
        };
        // 参数1 是任务对象; 参数2 是线程名字，推荐
        Thread t1 = new Thread(task1, "t1");
        t1.start();


        // lambda 精简代码
        Runnable task2 = () -> log.debug("hello");
        Thread t2 = new Thread(task2, "t1");
        t2.start();
    }
}
