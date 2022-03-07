package com.mochen.thread.example.notify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {

    /**
     * 核心代码
     * synchronized(lock) {
     *  while(条件不成立) {
     *  lock.wait();
     *  }
     *  // 干活
     * }
     * //另一个线程
     * synchronized(lock) {
     *  lock.notifyAll();
     * }
     */

    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟了，可以开始干活了");
            }
        }, "小南").start();

        new Thread(() -> {
            synchronized (room) {
                log.debug("外卖送到没？[{}]", hasTakeout);
                while (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖送到了，可以开始干活了");
            }
        }, "小女").start();

        Thread.sleep(1000);
        new Thread(() -> {
            synchronized (room) {
                hasTakeout = true;
                log.debug("外卖到了噢！");
                room.notifyAll();
            }
        }, "送外卖的").start();
    }

}
