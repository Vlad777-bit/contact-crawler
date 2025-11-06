package com.github.vlad777bit.contactcrawler.service;

import com.github.vlad777bit.contactcrawler.api.dto.ContactInfoResponse;
import com.github.vlad777bit.contactcrawler.api.dto.PageResponse;
import com.github.vlad777bit.contactcrawler.model.ContactInfo;
import com.github.vlad777bit.contactcrawler.model.ContactType;
import com.github.vlad777bit.contactcrawler.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ResultsService {

    private final ContactInfoRepository contactRepository;

    /**
     * Получить результаты для задачи с опциональной фильтрацией по типу и полнотекстовым "q",
     * с дедупликацией по normalizedValue. Постобработка выполняется в parallelStream().
     *
     * Замечания по производительности:
     * - БД фильтруем по обязательным параметрам (taskId, type) и сортируем по id (ASC).
     * - Доп. фильтр "q" и дедупликация делаем в памяти (CPU-bound) — это как раз работа для parallelStream().
     * - Сначала загружаем страницу БД (Pageable), затем фильтруем и уже поверх этого применяем "вторичную" пагинацию.
     */
    public PageResponse<ContactInfoResponse> getResults(
            Long taskId,
            ContactType type,
            String q,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(size, 200), Sort.by(Sort.Direction.ASC, "id"));
        Page<ContactInfo> dbPage = (type == null)
                ? contactRepository.findByTask_Id(taskId, pageable)
                : contactRepository.findByTask_IdAndType(taskId, type, pageable);

        // Тонкость: параллельные стримы эффективны для CPU-bound стадий (маппинг/фильтрация), а не для I/O. :contentReference[oaicite:2]{index=2}
        final String needle = (q == null || q.isBlank()) ? null : q.toLowerCase(Locale.ROOT);

        List<ContactInfoResponse> processed = dbPage.getContent()
                .parallelStream()
                .filter(ci -> {
                    if (needle == null) return true;
                    // простая "содержит" фильтрация по нескольким полям
                    return containsIgnoreCase(ci.getRawValue(), needle)
                            || containsIgnoreCase(ci.getNormalizedValue(), needle)
                            || containsIgnoreCase(ci.getSourceUrl(), needle)
                            || containsIgnoreCase(ci.getContext(), needle);
                })
                // дедупликация по normalizedValue (часто встречаются дубли при разных контекстах/страницах)
                .filter(StreamUtils.distinctByKey(ContactInfo::getNormalizedValue)) // thread-safe для parallelStream
                .sorted(Comparator.comparingLong(ContactInfo::getId)) // стабильный порядок после параллельной обработки
                .map(ContactInfoResponse::from)
                .toList(); // toList() подходит для параллельных стримов; редукция потокобезопасна для корректных коллекторов. :contentReference[oaicite:3]{index=3}

        // Вторичная пагинация поверх обработанного результата страницы БД
        int from = Math.min(page * pageable.getPageSize(), processed.size());
        int to = Math.min(from + pageable.getPageSize(), processed.size());
        List<ContactInfoResponse> slice = processed.subList(from, to);

        int totalPages = (int) Math.ceil(processed.size() / (double) pageable.getPageSize());
        return new PageResponse<>(slice, page, pageable.getPageSize(), processed.size(), totalPages);
    }

    private static boolean containsIgnoreCase(String source, String needleLower) {
        return source != null && source.toLowerCase(Locale.ROOT).contains(needleLower);
    }
}
