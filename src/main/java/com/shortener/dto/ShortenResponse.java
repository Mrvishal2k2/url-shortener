package com.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenResponse {
    private String shortId;
    private String shortUrl;
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;
}
