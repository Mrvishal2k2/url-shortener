package com.shortener.service;

import com.shortener.dto.UrlStats;
import com.shortener.errors.AlreadyExists;
import com.shortener.errors.ShortIdExpired;
import com.shortener.errors.ShortIdNotFound;
import com.shortener.model.Url;
import com.shortener.repository.ShortenerRepo;
import com.shortener.util.ShortenerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService {

    private final ShortenerRepo shortenerRepo;

    private final ShortenerUtils shortenerUtils;

    @Override
    public Url createShortUrl(String originalUrl, String shortId) {
        if (shortId != null && !shortId.isBlank()) {
            if (shortenerRepo.existsByShortId(shortId)) {
                throw new AlreadyExists("Short ID already exists");
            }
        }
        else{
            int maxRetries = 5;
            int attempts = 0;

            do {
                shortId = shortenerUtils.generateShortId();
                attempts++;
                if (attempts > maxRetries) {
                    throw new IllegalStateException("Failed to generate unique short ID after multiple attempts");
                }
            } while (shortenerRepo.existsByShortId(shortId));
        }

        Url url = new Url();
        url.setUrl(originalUrl);
        url.setShortId(shortId);
        url.setExpiresAt(Instant.now().plus(Duration.ofDays(30)));
        return shortenerRepo.save(url);

    }

    @Override
    public String getOriginalUrl(String shortId) throws ShortIdNotFound, ShortIdExpired {
        Url originalUrl = shortenerRepo.findByShortId(shortId)
                .orElseThrow(()-> new ShortIdNotFound(shortId));

        if(!originalUrl.isActive()){
            throw new ShortIdNotFound(shortId);
        }
        if (originalUrl.getExpiresAt().isBefore(Instant.now())) {
            throw new ShortIdExpired(shortId);
        }
        originalUrl.setClickCount(originalUrl.getClickCount()+1);
        shortenerRepo.save(originalUrl);

        return originalUrl.getUrl();
    }


    @Override
    public UrlStats getStats(String shortId) throws ShortIdNotFound {

        Url data = shortenerRepo.findByShortId(shortId).orElseThrow(
                ()-> new ShortIdNotFound(shortId + "Not found")
        );

        return UrlStats.builder()
                .url(data.getUrl())
                .clickCount(data.getClickCount())
                .shortId(data.getShortId())
                .createdAt(data.getCreatedAt())
                .expiresAt(data.getExpiresAt())
                .build();
    }

    @Override
    public String delete(String shortId) {
        Optional<Url> urlOpt = shortenerRepo.findByShortId(shortId);
        urlOpt.ifPresent(shortenerRepo::delete);
        return shortId;
    }
}
