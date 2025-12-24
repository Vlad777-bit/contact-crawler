package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.ContactInfo;
import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import com.github.vlad777bit.contactcrawler.metrics.CrawlMetrics;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final PageFetcher fetcher;
    private final ContactExtractionService extractor;
    private final ContactInfoRepository contactRepo;
    private final CrawlMetrics metrics;
    private final Executor crawlerExecutor;

    /** минимальная задержка между запросами к одному хосту (вежливость) */
    private static final long MIN_HOST_DELAY_MS = 500;

    /** карта последних обращений к хостам */
    private final ConcurrentHashMap<String, Long> hostLastAccess = new ConcurrentHashMap<>();

    /** Задача обхода: BFS до maxDepth/maxPages, параллельные загрузки страниц. */
    public void crawl(CrawlingTask task) {
        String root = UrlNormalizer.normalize(task.getSeedUrl());
        if (root == null) {
            log.warn("Seed URL is invalid: {}", task.getSeedUrl());
            return;
        }

        int maxDepth = task.getMaxDepth() != null ? task.getMaxDepth() : 1;
        int maxPages = task.getMaxPages() != null ? task.getMaxPages() : 100;

        record Node(String url, int depth) {}
        ConcurrentLinkedQueue<Node> frontier = new ConcurrentLinkedQueue<>();
        frontier.add(new Node(root, 0));

        Set<String> visited = ConcurrentHashMap.newKeySet();
        visited.add(root);

        AtomicInteger fetchedCount = new AtomicInteger(0);

        // простой «воркер-пул»: пока есть новые URL в очереди и не превышен лимит — забираем задания
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors() * 2; i++) {
            tasks.add(() -> {
                while (fetchedCount.get() < maxPages) {
                    Node node = frontier.poll();
                    if (node == null) break;
                    if (node.depth() > maxDepth) continue;
                    try {
                        throttleByHost(node.url());
                        Document doc = fetcher.fetch(node.url());
                        // базовый парсинг контактов
                        List<ContactInfo> contacts = extractor.extractAllFromHtml(doc.outerHtml(), node.url(), task);
                        persistBatch(contacts);

                        int count = fetchedCount.incrementAndGet();
                        if (count % 10 == 0) {
                            log.debug("Fetched {} pages", count);
                        }

                        // расширяем фронт ссылками
                        if (node.depth() < maxDepth) {
                            for (String link : LinkExtractor.extractAbsoluteLinks(doc)) {
                                if (visited.add(link)) {
                                    frontier.add(new Node(link, node.depth() + 1));
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.debug("Fetch failed {}: {}", node.url(), e.getMessage());
                    } catch (Exception e) {
                        log.warn("Processing failed {}: {}", node.url(), e.toString());
                    }
                }
            });
        }

        // запускаем все воркеры в кастомном пуле
        tasks.forEach(crawlerExecutor::execute);

        // грубая синхронизация завершения: ждём пока очередь опустеет или достигнут лимит
        // (в рабочем прод-варианте стоит использовать Phaser/CountDownLatch или CompletableFuture)
        while (!frontier.isEmpty() && fetchedCount.get() < maxPages) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /** Минимальная задержка между запросами к одному хосту (best-effort). */
    private void throttleByHost(String url) {
        String host = UrlNormalizer.host(url);
        if (host == null) return;
        long now = System.currentTimeMillis();
        hostLastAccess.compute(host, (h, last) -> {
            if (last == null) return now;
            long wait = last + MIN_HOST_DELAY_MS - now;
            if (wait > 0) {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return System.currentTimeMillis();
        });
    }

    @Transactional
    protected void persistBatch(List<ContactInfo> contacts) {
        if (contacts == null || contacts.isEmpty()) return;

        contactRepo.saveAll(contacts);
        metrics.recordSavedRecords(contacts.size());
    }
}
