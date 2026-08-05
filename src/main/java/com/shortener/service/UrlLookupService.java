package com.shortener.service;

import com.shortener.dto.CachedUrl;
import com.shortener.errors.ShortIdNotFound;
import com.shortener.model.Url;
import com.shortener.repository.ShortenerRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UrlLookupService {

    private final ShortenerRepo shortenerRepo;

    @Cacheable(cacheNames = "urls", key = "#shortId")
    @Transactional(readOnly = true)
    public CachedUrl findByShortId(String shortId) {
        Url url = shortenerRepo.findByShortId(shortId)
                .orElseThrow(() -> new ShortIdNotFound(shortId));
        return new CachedUrl(url.getUrl(), url.getExpiresAt(), url.isActive());
    }
}