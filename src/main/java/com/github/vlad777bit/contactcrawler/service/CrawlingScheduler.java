package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import com.github.vlad777bit.contactcrawler.repository.CrawlingTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class CrawlingScheduler {

    private final CrawlingTaskRepository taskRepository;
    private final CrawlingWorker worker;
    private final Executor executor;

    public CrawlingScheduler(
            CrawlingTaskRepository taskRepository,
            CrawlingWorker worker,
            @Qualifier("crawlerExecutor") Executor executor
    ) {
        this.taskRepository = taskRepository;
        this.worker = worker;
        this.executor = executor;
    }

    @Scheduled(cron = "${crawler.schedule.cron:*/10 * * * * *}")
    public void dispatchPendingTasks() {
        List<CrawlingTask> pending = taskRepository.findByStatus(CrawlingTaskStatus.PENDING);
        if (pending.isEmpty()) {
            log.debug("No pending tasks");
            return;
        }
        log.info("Dispatching {} pending tasks", pending.size());
        pending.forEach(task -> executor.execute(() -> worker.runTask(task)));
    }
}
