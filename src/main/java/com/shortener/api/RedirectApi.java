package com.shortener.api;

import com.shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RedirectApi {

    private final UrlService urlService;

    @GetMapping("/{shortId:^(?!scalar|v3|actuator|swagger-ui)[a-zA-Z0-9_-]+$}")
    public ResponseEntity<Void> redirect(@PathVariable("shortId") String shortId){
        String originalUrl = urlService.getOriginalUrl(shortId);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

}
