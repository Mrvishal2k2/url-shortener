package com.shortener.service;

import com.shortener.errors.ShortIdNotFound;
import com.shortener.model.Url;

public interface UrlService {

    Url createShortUrl(String originalUrl, String shortId);

    String getOriginalUrl(String shortId) throws ShortIdNotFound;

    Url getStats(String shortId);

    String delete(String shortId);
}