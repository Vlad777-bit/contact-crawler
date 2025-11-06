package com.github.vlad777bit.contactcrawler.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Ответственен за сетевой вызов и получение HTML-документа. */
@Component
public class PageFetcher {

    @Value("${crawler.http.userAgent:contact-crawler/1.0 (+https://example.invalid)}")
    private String userAgent;

    @Value("${crawler.http.timeoutMillis:5000}")
    private int timeoutMillis;

    @Value("${crawler.http.maxBodySizeBytes:1048576}") // 1MB
    private int maxBodySizeBytes;

    public Document fetch(String url) throws IOException {
        Connection conn = Jsoup.connect(url)
                .userAgent(userAgent)
                .timeout(timeoutMillis)
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .maxBodySize(maxBodySizeBytes)
                .referrer("https://www.google.com");

        Connection.Response resp = conn.execute();
        String contentType = resp.contentType();
        // фильтруем не-HTML (contentType может быть null)
        if (contentType == null || !contentType.toLowerCase().contains("text/html")) {
            throw new IOException("Non-HTML content: " + contentType);
        }
        // baseUri важен для abs:href
        return resp.parse();
    }
}
