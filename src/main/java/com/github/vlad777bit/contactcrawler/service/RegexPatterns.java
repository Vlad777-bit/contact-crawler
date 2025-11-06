package com.github.vlad777bit.contactcrawler.service;

import java.util.regex.Pattern;

public final class RegexPatterns {
    private RegexPatterns() {}

    public static final Pattern EMAIL =
            Pattern.compile("(?i)\\b[a-z0-9._%+\\-]+@[a-z0-9\\-]+(?:\\.[a-z0-9\\-]+)*\\.[a-z]{2,63}\\b");

    public static final Pattern PHONE_HUMAN =
            Pattern.compile("\\+?\\d[\\d\\s().\\-]{6,}\\d");

    /**
     * Адреса (обновлено):
     * - Разрешаем алфанумерический номер дома: 1–6 цифр + необязательная буква (напр., 221B).
     * - Добавляем тип улицы "parkway" (и распространённые сокращения) + несколько частых суффиксов.
     * - Флаг (?iu): Unicode + case-insensitive.
     *
     * Источники: USPS Publication 28 — список суффиксов (включая PARKWAY/PKWY) и упоминание алфанумерики в номерах домов.
     */
    public static final Pattern ADDRESS =
            Pattern.compile(
                    "(?iu)\\b\\d{1,6}[\\p{L}]?\\s+[\\p{L}0-9.'\\-]+\\s+" +
                            "(" +
                            "street|st\\.?" +
                            "|avenue|ave\\.?" +
                            "|road|rd\\.?" +
                            "|boulevard|blvd\\.?" +
                            "|lane|ln\\.?" +
                            "|drive|dr\\.?" +
                            "|parkway|pkwy\\.?" +    // добавлено
                            "|way" +
                            "|court|ct\\.?" +
                            "|place|pl\\.?" +
                            "|terrace|ter\\.?" +
                            ")" +
                            "\\b"
            );
}
