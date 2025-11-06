package com.github.vlad777bit.contactcrawler.service;

import java.util.regex.Pattern;

/**
 * Набор компилируемых паттернов для извлечения контактов.
 * Подобраны по практичным рекомендациям (OWASP для email, E.164 для телефонов).
 */
public final class RegexPatterns {
    private RegexPatterns() {}

    /**
     * Email (извлечение из текста): упрощённый и практичный паттерн на основе OWASP.
     * Мы используем границы слова и домены с TLD 2-63 символов.
     */
    public static final Pattern EMAIL =
            Pattern.compile("(?i)\\b[a-z0-9._%+\\-]+@[a-z0-9\\-]+(?:\\.[a-z0-9\\-]+)*\\.[a-z]{2,63}\\b");

    /**
     * Телефоны: сначала ловим «человеческий» формат (скобки/пробелы/дефисы),
     * затем нормализуем и валидируем по критериям E.164 (8–15 цифр, без лидирующего 0).
     * Этот паттерн шире, задача валидации — на этапе нормализации.
     */
    public static final Pattern PHONE_HUMAN =
            Pattern.compile("\\+?\\d[\\d\\s().\\-]{6,}\\d");

    /**
     * Адреса (эвристика): номер дома + название улицы + тип улицы.
     * Unicode-буквы поддерживаются через \\p{L}.
     */
    public static final Pattern ADDRESS =
            Pattern.compile("(?iu)\\b\\d{1,5}\\s+[\\p{L}0-9.'\\-]+\\s+(street|st\\.?|avenue|ave\\.?|road|rd\\.?|boulevard|blvd\\.?|lane|ln\\.?|drive|dr\\.?)(?:\\s+\\w+\\.?)?\\b");
}
