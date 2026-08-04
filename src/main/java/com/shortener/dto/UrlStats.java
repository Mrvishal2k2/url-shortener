package com.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlStats {
    private String url;
    private String shortId;
    private String shortUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private Long clickCount;
}
