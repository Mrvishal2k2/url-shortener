package com.shortener.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UrlStats {
    private String url;
    private String shortId;
    private String shortUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private Long clickCount;
}
