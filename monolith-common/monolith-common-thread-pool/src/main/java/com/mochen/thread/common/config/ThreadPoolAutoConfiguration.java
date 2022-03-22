package com.mochen.thread.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
public class ThreadPoolAutoConfiguration {

    /**
     * cpu 核心数量
     */
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    @Bean
    @ConditionalOnClass(ThreadPoolExecutor.class)
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(CPU_NUM,CPU_NUM * 2,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(99999));
    }

}
