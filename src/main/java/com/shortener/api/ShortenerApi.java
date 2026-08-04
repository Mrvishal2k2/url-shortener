package com.shortener.api;

import com.shortener.model.Url;
import com.shortener.service.UrlService;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenerApi {

    @Autowired
    private UrlService urlService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/shorten")
    public String shorten(@RequestParam Url url) {
        return urlService.createShortUrl(url.getUrl());
    }
}
