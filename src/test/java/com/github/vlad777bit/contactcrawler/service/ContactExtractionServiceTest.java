package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.model.ContactInfo;
import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContactExtractionServiceTest {

    private final ContactExtractionService service = new ContactExtractionService();

    private static CrawlingTask task() {
        return CrawlingTask.builder()
                .id(1L)
                .seedUrl("https://example.com")
                .status(CrawlingTaskStatus.PENDING)
                .build();
    }

    @Test
    @DisplayName("Извлечение email из HTML")
    void extractEmails() {
        String html = """
            <html><body>
              <p>Contact us at <a href="mailto:info@example.com">info@example.com</a></p>
            </body></html>
            """;
        List<ContactInfo> contacts = service.extractAllFromHtml(html, "https://example.com", task());
        assertThat(contacts)
                .anySatisfy(ci -> {
                    assertThat(ci.getType().name()).isEqualTo("EMAIL");
                    assertThat(ci.getNormalizedValue()).isEqualTo("info@example.com");
                });
    }

    @Test
    @DisplayName("Нормализация телефонов к E.164 (квази)")
    void extractPhones() {
        String html = """
            <html><body>
              <p>Call: +1 (555) 010-020</p>
            </body></html>
            """;
        List<ContactInfo> contacts = service.extractAllFromHtml(html, "https://example.com", task());
        assertThat(contacts)
                .anySatisfy(ci -> {
                    if ("PHONE".equals(ci.getType().name())) {
                        assertThat(ci.getNormalizedValue()).isIn("+1555010020", "1555010020");
                    }
                });
    }

    @Test
    @DisplayName("Эвристика для адресов: номер + улица + тип")
    void extractAddresses() {
        String html = """
            <html><body>
              <p>Office: 1600 Amphitheatre Parkway</p>
              <p>Branch: 221B Baker Street</p>
            </body></html>
            """;
        List<ContactInfo> contacts = service.extractAllFromHtml(html, "https://example.com", task());
        assertThat(contacts.stream().anyMatch(ci -> "ADDRESS".equals(ci.getType().name()))).isTrue();
    }
}
