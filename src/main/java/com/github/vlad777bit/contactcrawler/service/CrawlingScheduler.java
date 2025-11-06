package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import com.github.vlad777bit.contactcrawler.repository.CrawlingTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Планировщик, который периодически берёт PENDING-задачи из БД и отправляет их в пул.
 * Параметры расписания задаются в application.properties (см. crawler.schedule.*).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final CrawlingTaskRepository taskRepository;
    private final CrawlingWorker worker;

    @Qualifier("crawlerExecutor")
    private final Executor executor;

    /**
     * По умолчанию — каждые 10 секунд. Можно переопределить cron-строкой.
     * Важно: @Scheduled по умолчанию НЕ выполняет одну и ту же задачу параллельно,
     * но разные задачи мы отправляем в кастомный пул потоков.
     */
    @Scheduled(
            cron = "${crawler.schedule.cron:*/10 * * * * *}"
    )
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
