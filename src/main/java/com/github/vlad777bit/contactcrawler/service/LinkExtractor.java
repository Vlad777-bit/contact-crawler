package com.github.vlad777bit.contactcrawler.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/** Извлекает абсолютные ссылки из документа. */
final class LinkExtractor {
    private LinkExtractor() {}

    static List<String> extractAbsoluteLinks(Document doc) {
        List<String> out = new ArrayList<>();
        Elements links = doc.select("a[href]");
        for (Element a : links) {
            // jsoup abs:href -> абсолютный URL на основе baseUri документа
            String href = a.attr("abs:href");
            if (href == null || href.isBlank()) continue;
            String normalized = UrlNormalizer.normalize(href);
            if (normalized != null) out.add(normalized);
        }
        return out;
    }
}
