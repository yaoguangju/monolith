package com.mochen.thread.example.create;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {
    // 直接使用Thread类
    public static void main(String[] args) {
        // 正常写法
        Thread t1 = new Thread(){
            @Override
            public void run() {
                log.info("t1.running");
            }
        };
        t1.start();

        // lambda 精简代码
        Thread t2 = new Thread(() -> log.info("t2.running"));
        t2.start();

        // 线程命名
        Thread t3 = new Thread("t3"){
            @Override
            public void run() {
                log.info("t3.running");
            }
        };
        t3.start();


    }
}
