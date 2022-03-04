package com.mochen.thread.example.interrupt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {
    // 打断sleep中的线程
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "t1");
        t1.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();
        log.debug(" 打断状态: {}", t1.isInterrupted());
    }

}
