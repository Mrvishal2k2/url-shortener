package com.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllUrls
{
  private int statusCode=200;
  private Instant timestamp=Instant.now();
  private long entriesCount;
  private List<UrlStats> urlMapping;
}
