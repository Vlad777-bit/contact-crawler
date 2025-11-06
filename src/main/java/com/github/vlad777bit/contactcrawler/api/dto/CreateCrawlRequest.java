package com.github.vlad777bit.contactcrawler.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateCrawlRequest(
        @NotBlank
        @Size(max = 1024)
        @URL
        String seedUrl,

        @Min(0) @Max(8) Integer maxDepth,
        @Min(1) @Max(10_000) Integer maxPages
) {}
