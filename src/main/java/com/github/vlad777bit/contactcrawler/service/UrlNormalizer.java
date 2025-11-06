package com.github.vlad777bit.contactcrawler.service;

import java.net.URI;
import java.net.URISyntaxException;

final class UrlNormalizer {
    private UrlNormalizer() {}

    /** Приводим URL к каноническому виду: схема/хост в нижнем регистре, убираем фрагмент, нормализуем путь. */
    static String normalize(String url) {
        try {
            URI u = new URI(url.trim());
            if (u.getScheme() == null || u.getHost() == null) return null;
            String scheme = u.getScheme().toLowerCase();
            String host = u.getHost().toLowerCase();
            int port = u.getPort();
            String path = (u.getPath() == null || u.getPath().isBlank()) ? "/" : u.getPath();
            // Убираем фрагмент и query оставляем как есть
            String query = u.getQuery();
            String authority = (port == -1) ? host : host + ":" + port;
            URI out = new URI(scheme, authority, path, query, null);
            return out.toString();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    static String host(String url) {
        try {
            return new URI(url).getHost();
        } catch (Exception e) {
            return null;
        }
    }
}
