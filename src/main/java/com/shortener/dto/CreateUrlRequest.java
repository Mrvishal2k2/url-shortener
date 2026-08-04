package com.shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlRequest {
    @NotBlank(message = "URL is required")
    @Size(max=2048)
    private String url;

    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,10}$")
    private String shortId;
}
