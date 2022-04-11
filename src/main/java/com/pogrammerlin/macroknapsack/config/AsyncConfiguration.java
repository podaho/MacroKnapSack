package com.pogrammerlin.macroknapsack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration extends AsyncConfigurerSupport {
    @Value("${async.pool.coreSize}")
    Integer corePoolSize;

    @Value("${async.pool.maxSize}")
    Integer maxPoolSize;

    @Value("${async.pool.queueCapacity}")
    Integer queueCapacity;

    @Value("${async.shutdown.waitBeforeShutdown}")
    Boolean waitBeforeShutdown;

    @Value("${async.shutdown.waitSeconds}")
    Integer waitSeconds;

    @Override
    @Bean(name = "asyncExecutor", destroyMethod = "shutdown")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(waitBeforeShutdown);
        executor.setAwaitTerminationSeconds(waitSeconds);
        executor.setThreadNamePrefix("match-planning-cart-");
        executor.initialize();
        return executor;
    }
}
