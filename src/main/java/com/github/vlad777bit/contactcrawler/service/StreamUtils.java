package com.github.vlad777bit.contactcrawler.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Утилиты для работы со Stream API.
 */
public final class StreamUtils {
    private StreamUtils() {}

    /**
     * Дедупликация по произвольному ключу (thread-safe для parallelStream()).
     * Идея: ConcurrentHashMap для фиксации "увиденных" ключей.
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
