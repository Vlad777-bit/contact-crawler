package com.github.vlad777bit.contactcrawler.api.dto;

import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;

import java.time.Instant;

public record CrawlingTaskResponse(
        Long id,
        String seedUrl,
        Integer maxDepth,
        Integer maxPages,
        CrawlingTaskStatus status,
        Instant createdAt
) {
    public static CrawlingTaskResponse from(CrawlingTask t) {
        return new CrawlingTaskResponse(
                t.getId(),
                t.getSeedUrl(),
                t.getMaxDepth(),
                t.getMaxPages(),
                t.getStatus(),
                t.getCreatedAt()
        );
    }
}
