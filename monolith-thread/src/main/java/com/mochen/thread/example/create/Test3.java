package com.mochen.thread.example.create;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class Test3 {
    // FutureTask 配合 Thread
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task1 = new FutureTask<>(() ->{
            log.debug("hello");
            return 100;
        });

        new Thread(task1,"t1").start();

        Integer result = task1.get();
        log.debug("结果是，{}",result);
    }
}
