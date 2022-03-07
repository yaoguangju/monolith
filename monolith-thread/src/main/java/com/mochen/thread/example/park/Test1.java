package com.mochen.thread.example.park;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {

            log.debug("start...");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");

        },"t1");
        t1.start();

        Thread.sleep(1000);
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
