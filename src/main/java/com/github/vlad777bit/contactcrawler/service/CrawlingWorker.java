package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import com.github.vlad777bit.contactcrawler.repository.CrawlingTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingWorker {

    private final CrawlingTaskRepository taskRepository;
    private final CrawlerService crawlerService;

    public void runTask(CrawlingTask task) {
        try {
            log.info("Start crawling task id={} url={}", task.getId(), task.getSeedUrl());
            task.setStatus(CrawlingTaskStatus.RUNNING);
            task.setStartedAt(Instant.now());
            taskRepository.save(task);

            crawlerService.crawl(task);

            task.setStatus(CrawlingTaskStatus.COMPLETED);
            task.setFinishedAt(Instant.now());
            taskRepository.save(task);
            log.info("Complete crawling task id={}", task.getId());
        } catch (Exception e) {
            failTask(task, e.getMessage());
            Thread.currentThread().interrupt(); // на случай, если нас прервали снаружи
        }
    }

    private void failTask(CrawlingTask task, String reason) {
        log.warn("Fail crawling task id={} reason={}", task.getId(), reason);
        task.setStatus(CrawlingTaskStatus.FAILED);
        task.setFinishedAt(Instant.now());
        task.setErrorMessage(reason);
        taskRepository.save(task);
    }
}
