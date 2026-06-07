package com.mf.framework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 统一线程池 —— 所有 @Async 方法共用。
 * 核心线程数 = CPU 核数，最大线程数 = CPU × 2。
 * 队列满了由调用线程执行，防止丢任务。
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores);
        executor.setMaxPoolSize(cores * 2);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("mf-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
