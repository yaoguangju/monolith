package com.mochen.thread.example.interrupt;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.TestTwoPhaseTermination")
public class TestTwoPhaseTermination {
    public static void main(String[] args) throws InterruptedException {
        TPTInterrupt t = new TPTInterrupt();
        t.start();

        Thread.sleep(3500);
        log.debug("stop");
        t.stop();
    }
}

@Slf4j(topic = "c.TPTInterrupt")
class TPTInterrupt {
    private Thread thread;

    public void start(){
        thread = new Thread(() -> {
            while(true) {
                Thread current = Thread.currentThread();
                // 如果当前线程打断标记为true
                if(current.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("将结果保存");
                } catch (InterruptedException e) {
                    // 如果打断的线程正在sleep，捕捉异常（此时打断标记为false），将打断标记置为true
                    current.interrupt();
                }

            }
        },"监控线程");
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}



