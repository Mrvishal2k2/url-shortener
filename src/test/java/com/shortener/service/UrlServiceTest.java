package com.shortener.service;

import com.shortener.dto.AllUrls;
import com.shortener.dto.UrlStats;
import com.shortener.errors.AlreadyExists;
import com.shortener.errors.NotValidLink;
import com.shortener.errors.ShortIdExpired;
import com.shortener.errors.ShortIdNotFound;
import com.shortener.model.Url;
import com.shortener.repository.ShortenerRepo;
import com.shortener.util.ShortenerUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {
    @Mock
    ShortenerRepo shortenerRepo;
    @InjectMocks
    UrlServiceImpl urlService;
    @Mock
    ShortenerUtils shortenerUtils;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(urlService, "expiryGuest", 30L);
    }

    // createShortId
    @Test
    void createShortURLSuccess() {
        Url urls = new Url();
        urls.setUrl("https://google.com");
        urls.setShortId("google");

        Mockito.when(shortenerRepo.save(Mockito.any(Url.class))).thenReturn(urls);
        Url addedUrl = urlService.createShortUrl(urls.getUrl(), urls.getShortId());

        Assertions.assertNotNull(addedUrl);
        Assertions.assertEquals(urls.getShortId(), addedUrl.getShortId());
        Assertions.assertEquals(urls.getUrl(), addedUrl.getUrl());

        verify(shortenerRepo).save(any(Url.class));
        verify(shortenerRepo).existsByShortId("google");

//        Assertions.assertTrue(addedUrl.getShortId().equals("google"));

    }

    @Test
    void createShortUrl_ShouldThrowAlreadyExists_WhenShortIdExists() {

        Mockito.when(shortenerRepo.existsByShortId("google"))
                .thenReturn(true);

        Assertions.assertThrows(
                AlreadyExists.class,
                () -> urlService.createShortUrl("https://google.com", "google")
        );

        Mockito.verify(shortenerRepo).existsByShortId("google");
        Mockito.verify(shortenerRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createShortUrl_ShouldGenerateShortId_WhenShortIdIsNull() {

        Url url = new Url();
        url.setUrl("https://google.com");
        url.setShortId("abc123");

        Mockito.when(shortenerUtils.generateShortId())
                .thenReturn("abc123");

        Mockito.when(shortenerRepo.existsByShortId("abc123"))
                .thenReturn(false);

        Mockito.when(shortenerRepo.save(Mockito.any(Url.class)))
                .thenReturn(url);

        Url result = urlService.createShortUrl("https://google.com", null);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("abc123", result.getShortId());

        Mockito.verify(shortenerUtils).generateShortId();
        Mockito.verify(shortenerRepo).save(Mockito.any(Url.class));
    }

    @Test
    void createShortUrl_ShouldRetry_WhenGeneratedShortIdExists() {

        Url url = new Url();
        url.setUrl("https://google.com");
        url.setShortId("xyz789");

        Mockito.when(shortenerUtils.generateShortId())
                .thenReturn("abc123")
                .thenReturn("xyz789");

        Mockito.when(shortenerRepo.existsByShortId("abc123"))
                .thenReturn(true);

        Mockito.when(shortenerRepo.existsByShortId("xyz789"))
                .thenReturn(false);

        Mockito.when(shortenerRepo.save(Mockito.any(Url.class)))
                .thenReturn(url);

        Url result = urlService.createShortUrl("https://google.com", null);

        Assertions.assertEquals("xyz789", result.getShortId());

        Mockito.verify(shortenerUtils, Mockito.times(2)).generateShortId();
    }

    @Test
    void createShortUrl_ShouldThrowIllegalState_WhenRetryLimitExceeded() {

        Mockito.when(shortenerUtils.generateShortId())
                .thenReturn("abc123");

        Mockito.when(shortenerRepo.existsByShortId("abc123"))
                .thenReturn(true);

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> urlService.createShortUrl("https://google.com", null)
        );

        Mockito.verify(shortenerUtils, Mockito.times(6)).generateShortId();
        Mockito.verify(shortenerRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createShortUrl_ShouldThrowNotValidLink_WhenUrlIsInvalid() {

        Assertions.assertThrows(
                NotValidLink.class,
                () -> urlService.createShortUrl("invalid-url", "google")
        );

        Mockito.verify(shortenerRepo, Mockito.never()).save(Mockito.any());
    }


    //get original Url
    @Test
    void getOriginalUrl_ShouldReturnOriginalUrl() {

        Url url = new Url();
        url.setUrl("https://google.com");
        url.setShortId("google");
        url.setActive(true);
        url.setClickCount(0L);
        url.setExpiresAt(Instant.now().plus(Duration.ofDays(1)));

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.of(url));

        String result = urlService.getOriginalUrl("google");

        Assertions.assertEquals("https://google.com", result);

        Mockito.verify(shortenerRepo).findByShortId("google");
        Mockito.verify(shortenerRepo).incrementClickCount("google");
        Mockito.verify(shortenerRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getOriginalUrl_ShouldThrow_WhenShortIdNotFound() {

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ShortIdNotFound.class,
                () -> urlService.getOriginalUrl("google")
        );

        Mockito.verify(shortenerRepo).findByShortId("google");
        Mockito.verify(shortenerRepo, Mockito.never()).save(Mockito.any());
    }


    @Test
    void getOriginalUrl_ShouldThrow_WhenExpired() {

        Url url = new Url();
        url.setUrl("https://google.com");
        url.setShortId("google");
        url.setActive(true);
        url.setExpiresAt(Instant.now().minus(Duration.ofDays(1)));

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.of(url));

        Assertions.assertThrows(
                ShortIdExpired.class,
                () -> urlService.getOriginalUrl("google")
        );

        Mockito.verify(shortenerRepo, Mockito.never()).save(Mockito.any());
    }


    // all entries
    @Test
    void getAllUrls_ShouldReturnAllUrls() {

        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080");

        Url url1 = new Url();
        url1.setUrl("https://google.com");
        url1.setShortId("google");
        url1.setClickCount(5L);

        Url url2 = new Url();
        url2.setUrl("https://github.com");
        url2.setShortId("github");
        url2.setClickCount(10L);

        Mockito.when(shortenerRepo.findAll())
                .thenReturn(List.of(url1, url2));

        AllUrls result = urlService.getAllUrls();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getEntriesCount());
        Assertions.assertEquals(2, result.getUrlMapping().size());

        Assertions.assertEquals("google", result.getUrlMapping().getFirst().getShortId());
        Assertions.assertEquals("http://localhost:8080/google",
                result.getUrlMapping().getFirst().getShortUrl());

        Mockito.verify(shortenerRepo).findAll();
    }

    // stats of requested shortId
    @Test
    void getStats_ShouldReturnStats() {

        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080");

        Url url = new Url();
        url.setUrl("https://google.com");
        url.setShortId("google");
        url.setClickCount(15L);

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.of(url));

        UrlStats stats = urlService.getStats("google");

        Assertions.assertNotNull(stats);
        Assertions.assertEquals("google", stats.getShortId());
        Assertions.assertEquals("https://google.com", stats.getUrl());
        Assertions.assertEquals(15, stats.getClickCount());
        Assertions.assertEquals("http://localhost:8080/google", stats.getShortUrl());

        Mockito.verify(shortenerRepo).findByShortId("google");
    }

    @Test
    void getStats_ShouldThrow_WhenShortIdNotFound() {

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ShortIdNotFound.class,
                () -> urlService.getStats("google")
        );

        Mockito.verify(shortenerRepo).findByShortId("google");
    }

    // delete shortId
    @Test
    void delete_ShouldDeleteUrl_WhenShortIdExists() {
        Url url = new Url();
        url.setShortId("google");

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.of(url));

        String result = urlService.delete("google");

        Assertions.assertEquals("google", result);

        verify(shortenerRepo).findByShortId("google");
        verify(shortenerRepo).delete(url);
    }

    @Test
    void delete_ShouldNotDelete_WhenShortIdDoesNotExist() {

        Mockito.when(shortenerRepo.findByShortId("google"))
                .thenReturn(Optional.empty());

        String result = urlService.delete("google");

        Assertions.assertEquals("google", result);

        verify(shortenerRepo).findByShortId("google");
        verify(shortenerRepo, Mockito.never()).delete(Mockito.any());
    }

}
