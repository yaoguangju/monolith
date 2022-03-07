package com.mochen.thread.example.join;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {

    static int r1 = 0;
    static int r2 = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        });
        long start = System.currentTimeMillis();
        t1.start();
        // join时效等够了,会直接r1=10
        t1.join();
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }

}
