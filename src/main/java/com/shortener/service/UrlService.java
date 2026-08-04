package com.shortener.service;

import com.shortener.dto.UrlStats;
import com.shortener.errors.ShortIdNotFound;
import com.shortener.model.Url;

public interface UrlService {

    Url createShortUrl(String originalUrl, String shortId);

    String getOriginalUrl(String shortId) throws ShortIdNotFound;

    UrlStats getStats(String shortId) throws ShortIdNotFound;

    String delete(String shortId);
}