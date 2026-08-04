package com.shortener.service;

import com.shortener.model.Url;
import com.shortener.repository.ShortenerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private ShortenerRepo shortenerRepo;


    @Override
    public Url createShortUrl(String originalUrl) {
        return new Url();
    }

    @Override
    public String getOriginalUrl(String shortId) {
        return "";
    }

    @Override
    public void incrementClickCount(String shortId) {

    }

    @Override
    public Url getStats(String shortId) {
        return null;
    }

    @Override
    public void delete(String shortId) {

    }
}
