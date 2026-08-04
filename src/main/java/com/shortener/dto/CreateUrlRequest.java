package com.shortener.dto;

import lombok.Data;

@Data
public class CreateUrlRequest {
    private String url;
    private String shortId;
}
