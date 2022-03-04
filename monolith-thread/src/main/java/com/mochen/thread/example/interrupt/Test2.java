package com.mochen.thread.example.interrupt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test2 {
    // 打断正在运行的进程
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            while(true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                if(interrupted) {
                    log.debug(" 打断状态: {}", interrupted);
                    break;
                }
            }
        }, "t1");
        t1.start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();

    }

}
