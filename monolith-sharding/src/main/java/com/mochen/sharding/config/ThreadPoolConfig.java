package com.mochen.sharding.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 姚广举
 * @since 2022/1/19 17:27:57
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {

    /**
     * cpu 核心数量
     */
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池配置
     */
    @Bean("asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        executor.setCorePoolSize(CPU_NUM);
        // 配置最大线程池数量
        executor.setMaxPoolSize(CPU_NUM * 2);
        /// 线程池所使用的缓冲队列
        executor.setQueueCapacity(99999);

        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        executor.setAwaitTerminationSeconds(60);
        // 空闲线程存活时间
        executor.setKeepAliveSeconds(60);
        // 等待任务在关机时完成--表明等待所有线程执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池名称前缀
        executor.setThreadNamePrefix("thread-pool-");
        // 线程池拒绝策略(非拒绝策略，要处理全部的请求)
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        executor.initialize();
        log.info("线程池初始化......");
        return executor;
    }

}
