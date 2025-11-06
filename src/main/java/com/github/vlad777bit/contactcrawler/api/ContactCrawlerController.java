package com.github.vlad777bit.contactcrawler.api;

import com.github.vlad777bit.contactcrawler.api.dto.ContactInfoResponse;
import com.github.vlad777bit.contactcrawler.api.dto.CrawlingTaskResponse;
import com.github.vlad777bit.contactcrawler.api.dto.CreateCrawlRequest;
import com.github.vlad777bit.contactcrawler.api.dto.PageResponse;
import com.github.vlad777bit.contactcrawler.model.ContactType;
import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import com.github.vlad777bit.contactcrawler.repository.CrawlingTaskRepository;
import com.github.vlad777bit.contactcrawler.service.ResultsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping
public class ContactCrawlerController {

    private final CrawlingTaskRepository taskRepository;
    private final ResultsService resultsService;

    public ContactCrawlerController(CrawlingTaskRepository taskRepository, ResultsService resultsService) {
        this.taskRepository = taskRepository;
        this.resultsService = resultsService;
    }

    @PostMapping("/crawl")
    public ResponseEntity<CrawlingTaskResponse> createCrawl(
            @Valid @RequestBody CreateCrawlRequest req,
            UriComponentsBuilder ucb
    ) {
        CrawlingTask task = CrawlingTask.builder()
                .seedUrl(req.seedUrl())
                .maxDepth(req.maxDepth())
                .maxPages(req.maxPages())
                .status(CrawlingTaskStatus.PENDING)
                .build();

        task = taskRepository.save(task);

        URI location = ucb.path("/results")
                .queryParam("taskId", task.getId())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(CrawlingTaskResponse.from(task));
    }

    /**
     * GET /results?taskId=1&type=EMAIL&q=example&page=0&size=50
     * q — опциональный фильтр по подстроке (raw/normalized/sourceUrl/context), применяется в parallelStream().
     */
    @GetMapping("/results")
    public PageResponse<ContactInfoResponse> getResults(
            @RequestParam("taskId") Long taskId,
            @RequestParam(value = "type", required = false) ContactType type,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size
    ) {
        if (Objects.isNull(taskId)) {
            throw new IllegalArgumentException("taskId is required");
        }
        return resultsService.getResults(taskId, type, q, page, size);
    }
}
