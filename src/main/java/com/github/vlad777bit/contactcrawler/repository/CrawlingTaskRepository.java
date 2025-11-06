package com.github.vlad777bit.contactcrawler.repository;

import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlingTaskRepository extends JpaRepository<CrawlingTask, Long> {
    List<CrawlingTask> findByStatus(CrawlingTaskStatus status);
}
