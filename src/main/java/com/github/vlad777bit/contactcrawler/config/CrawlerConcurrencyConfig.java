package com.github.vlad777bit.contactcrawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
public class CrawlerConcurrencyConfig {

    /**
     * Пул потоков для рабочих задач краулинга.
     * Настраиваем фиксированное ядро, ограничиваем максимум и очередь.
     */
    @Bean(name = "crawlerExecutor")
    public Executor crawlerExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setThreadNamePrefix("crawler-exec-");
        exec.setCorePoolSize(8);
        exec.setMaxPoolSize(16);
        exec.setQueueCapacity(200);
        exec.setKeepAliveSeconds(30);
        exec.setWaitForTasksToCompleteOnShutdown(true);
        exec.setAwaitTerminationSeconds(10);
        exec.initialize();
        return exec;
    }

    /**
     * Планировщик для @Scheduled-задач (не рабочих задач краулера).
     * По умолчанию 1 поток; увеличиваем, чтобы несколько триггеров могли выполняться параллельно.
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("crawler-sched-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(10);
        return scheduler;
    }
}
