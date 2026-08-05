package com.shortener.api;

import com.shortener.dto.AllUrls;
import com.shortener.dto.CreateUrlRequest;
import com.shortener.dto.ShortenResponse;
import com.shortener.dto.UrlStats;
import com.shortener.errors.ShortIdNotFound;
import com.shortener.model.Url;
import com.shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShortenerApi {

    private final UrlService urlService;

    @Value("${app.base-url}")
    private String baseUrl;

    final Pattern PATTERN =
            Pattern.compile ("^[A-Za-z0-9_-]{3,10}$");


    @GetMapping("/ping")
    public String ping() {
        log.info("ping");
        return "pong";
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody CreateUrlRequest url) {
        Url urls = urlService.createShortUrl(url.getUrl(), url.getShortId());

        ShortenResponse response = ShortenResponse.builder()
                .originalUrl(urls.getUrl())
                .shortId(urls.getShortId())
                .shortUrl(baseUrl + "/" + urls.getShortId())
                .createdAt(urls.getCreatedAt())
                .expiresAt(urls.getExpiresAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<AllUrls> getAllUrls() {
        return ResponseEntity.ok(urlService.getAllUrls());
    }

    @GetMapping("/{shortId}/analysis")
    public ResponseEntity<UrlStats> analysis(@PathVariable("shortId") String shortId){

        if (!PATTERN.matcher(shortId).matches()) {
                throw new ShortIdNotFound("Invalid shortId");
        }
        UrlStats urlStats = urlService.getStats(shortId);
        urlStats.setShortUrl(baseUrl+ "/" + shortId);
        return ResponseEntity.status(HttpStatus.OK).body(urlStats);
    }

    @DeleteMapping("/{shortId}")
    public ResponseEntity<String> delete(@PathVariable("shortId") String shortId){

        if (!PATTERN.matcher(shortId).matches()) {
            throw new ShortIdNotFound("Invalid shortId");
        }

        String response = urlService.delete(shortId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(response+ " is deleted");
    }
}
