package com.shortener.service;

import com.shortener.dto.AllUrls;
import com.shortener.dto.UrlStats;
import com.shortener.model.Url;

public interface UrlService {

    Url createShortUrl(String originalUrl, String shortId);

    String getOriginalUrl(String shortId);

    AllUrls getAllUrls();

    UrlStats getStats(String shortId);

    String delete(String shortId);
}