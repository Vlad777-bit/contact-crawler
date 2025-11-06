package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import com.github.vlad777bit.contactcrawler.repository.CrawlingTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Временная рабочая «заглушка».
 * На следующем этапе будет заменён реальным многопоточным краулером.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingWorker {

    private final CrawlingTaskRepository taskRepository;

    public void runTask(CrawlingTask task) {
        try {
            log.info("Start crawling task id={} url={}", task.getId(), task.getSeedUrl());
            task.setStatus(CrawlingTaskStatus.RUNNING);
            task.setStartedAt(Instant.now());
            taskRepository.save(task);

            // TODO: здесь будет вызов реального краулера
            // Имитация небольшой работы
            Thread.sleep(10);

            task.setStatus(CrawlingTaskStatus.COMPLETED);
            task.setFinishedAt(Instant.now());
            taskRepository.save(task);
            log.info("Complete crawling task id={}", task.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            failTask(task, "Interrupted");
        } catch (Exception e) {
            failTask(task, e.getMessage());
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
