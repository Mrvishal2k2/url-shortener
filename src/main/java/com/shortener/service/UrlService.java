package com.shortener.service;

import com.shortener.model.Url;

public interface UrlService {

    Url createShortUrl(String originalUrl);

    String getOriginalUrl(String shortId);

    void incrementClickCount(String shortId);

    Url getStats(String shortId);

    void delete(String shortId);
}