package com.github.vlad777bit.contactcrawler.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vlad777bit.contactcrawler.api.dto.PageResponse;
import com.github.vlad777bit.contactcrawler.api.dto.ContactInfoResponse;
import com.github.vlad777bit.contactcrawler.model.ContactType;
import com.github.vlad777bit.contactcrawler.model.CrawlingTask;
import com.github.vlad777bit.contactcrawler.model.CrawlingTaskStatus;
import com.github.vlad777bit.contactcrawler.repository.CrawlingTaskRepository;
import com.github.vlad777bit.contactcrawler.service.ResultsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Срез-тест веб-слоя: поднимаем только MVC-компоненты.
 * Используем @WebMvcTest + MockMvc + @MockBean для зависимостей контроллера.
 */
@WebMvcTest(ContactCrawlerController.class)
class ContactCrawlerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrawlingTaskRepository taskRepository;

    @MockBean
    private ResultsService resultsService;

    @Test
    @DisplayName("POST /crawl -> 201 Created + Location + тело задачи")
    void createCrawl_returns201AndLocation() throws Exception {
        // given: мок сохранения задачи
        CrawlingTask saved = CrawlingTask.builder()
                .id(42L)
                .seedUrl("https://example.com")
                .status(CrawlingTaskStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        when(taskRepository.save(ArgumentMatchers.any(CrawlingTask.class))).thenReturn(saved);

        String json = """
            {
              "seedUrl": "https://example.com",
              "maxDepth": 1,
              "maxPages": 10
            }
            """;

        // when/then
        mvc.perform(post("/crawl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/results?taskId=42")))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.seedUrl").value("https://example.com"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /results -> пагинация + фильтрация по type и q")
    void getResults_returnsPage() throws Exception {
        PageResponse<ContactInfoResponse> mockPage = new PageResponse<>(
                List.of(new ContactInfoResponse(1L, ContactType.EMAIL, "info@example.com",
                        "info@example.com", "https://example.com", "footer", Instant.now())),
                0, 50, 1, 1
        );
        when(resultsService.getResults(42L, ContactType.EMAIL, "example", 0, 50))
                .thenReturn(mockPage);

        mvc.perform(get("/results")
                        .param("taskId", "42")
                        .param("type", "EMAIL")
                        .param("q", "example")
                        .param("page", "0")
                        .param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].type").value("EMAIL"))
                .andExpect(jsonPath("$.content[0].normalizedValue").value("info@example.com"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
