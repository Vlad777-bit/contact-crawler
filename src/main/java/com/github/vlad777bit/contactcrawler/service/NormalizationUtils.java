package com.github.vlad777bit.contactcrawler.service;

import java.util.Locale;

public final class NormalizationUtils {
    private NormalizationUtils() {}

    /** Email: нижний регистр, обрезка пробелов */
    public static String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Телефон: оставить только цифры, при наличии плюса — сохранить его,
     * затем валидация по длине E.164 (макс. 15 цифр, не начинаться с 0).
     */
    public static String normalizePhone(String phoneRaw) {
        if (phoneRaw == null) return null;
        String trimmed = phoneRaw.trim();
        boolean hasPlus = trimmed.startsWith("+");
        String digits = trimmed.replaceAll("\\D", "");
        if (digits.isEmpty()) return null;

        // E.164: до 15 цифр, первая — 1-9
        if (digits.length() < 8 || digits.length() > 15 || digits.charAt(0) == '0') {
            return null;
        }
        return hasPlus ? "+" + digits : digits;
    }

    /** Адрес: схлопнуть пробелы, тримминг; без агрессивной нормализации. */
    public static String normalizeAddress(String addr) {
        if (addr == null) return null;
        return addr.replaceAll("\\s+", " ").trim();
    }
}
