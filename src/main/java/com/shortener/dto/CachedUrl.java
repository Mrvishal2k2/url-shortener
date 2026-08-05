package com.shortener.dto;

import java.time.Instant;

public record CachedUrl(String url, Instant expiresAt, boolean active) {}