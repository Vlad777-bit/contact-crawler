package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.ContactInfo;
import com.github.vlad777bit.contactcrawler.model.ContactType;
import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

@Service
public class ContactExtractionService {

    /**
     * Извлечь контакты из HTML (email/телефон/адрес) и вернуть как модели ContactInfo (без сохранения).
     * HTML -> видимый текст через jsoup -> regex search -> нормализация -> дедупликация.
     */
    public List<ContactInfo> extractAllFromHtml(String html, String sourceUrl, CrawlingTask task) {
        String text = htmlToVisibleText(html);
        List<ContactInfo> result = new ArrayList<>();
        result.addAll(extractEmails(text, sourceUrl, task));
        result.addAll(extractPhones(text, sourceUrl, task));
        result.addAll(extractAddresses(text, sourceUrl, task));
        return result;
    }

    public List<ContactInfo> extractEmails(String text, String sourceUrl, CrawlingTask task) {
        Set<String> seen = new LinkedHashSet<>();
        List<ContactInfo> out = new ArrayList<>();
        Matcher m = RegexPatterns.EMAIL.matcher(text);
        while (m.find()) {
            String raw = m.group();
            String normalized = NormalizationUtils.normalizeEmail(raw);
            if (normalized != null && seen.add(normalized)) {
                out.add(build(ContactType.EMAIL, raw, normalized, sourceUrl, task));
            }
        }
        return out;
    }

    public List<ContactInfo> extractPhones(String text, String sourceUrl, CrawlingTask task) {
        Set<String> seen = new LinkedHashSet<>();
        List<ContactInfo> out = new ArrayList<>();
        Matcher m = RegexPatterns.PHONE_HUMAN.matcher(text);
        while (m.find()) {
            String raw = m.group();
            String normalized = NormalizationUtils.normalizePhone(raw);
            if (normalized != null && seen.add(normalized)) {
                out.add(build(ContactType.PHONE, raw, normalized, sourceUrl, task));
            }
        }
        return out;
    }

    public List<ContactInfo> extractAddresses(String text, String sourceUrl, CrawlingTask task) {
        Set<String> seen = new LinkedHashSet<>();
        List<ContactInfo> out = new ArrayList<>();
        Matcher m = RegexPatterns.ADDRESS.matcher(text);
        while (m.find()) {
            String raw = m.group();
            String normalized = NormalizationUtils.normalizeAddress(raw);
            if (normalized != null && seen.add(normalized.toLowerCase())) {
                out.add(build(ContactType.ADDRESS, raw, normalized, sourceUrl, task));
            }
        }
        return out;
    }

    private static String htmlToVisibleText(String html) {
        if (html == null || html.isBlank()) return "";
        // jsoup корректно удалит теги/скрипты/стили и вернёт читаемый текст
        return Jsoup.parse(html).text();
    }

    private static ContactInfo build(ContactType type, String raw, String normalized, String sourceUrl, CrawlingTask task) {
        return ContactInfo.builder()
                .type(type)
                .rawValue(raw)
                .normalizedValue(normalized)
                .sourceUrl(sourceUrl)
                .task(task)
                .build();
    }
}
