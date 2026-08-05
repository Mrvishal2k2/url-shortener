package com.shortener.api;

import com.shortener.dto.AllUrls;
import com.shortener.dto.CreateUrlRequest;
import com.shortener.dto.UrlStats;
import com.shortener.model.Url;
import com.shortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;


@WebMvcTest(ShortenerApi.class)
class ShortenerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ShortenerApi shortenerApi;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(shortenerApi, "baseUrl", "http://localhost:8080");
    }

    @Test
    void shorten_ShouldReturnCreated() throws Exception {

        CreateUrlRequest request = new CreateUrlRequest();
        request.setUrl("https://google.com");
        request.setShortId("google");

        Url url = new Url();
        url.setUrl("https://google.com");
        url.setShortId("google");
        url.setCreatedAt(Instant.now());

        Mockito.when(urlService.createShortUrl(
                "https://google.com",
                "google"
        )).thenReturn(url);

        mockMvc.perform(post("/api/shorten")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value("https://google.com"))
                .andExpect(jsonPath("$.shortId").value("google"))
                .andExpect(jsonPath("$.shortUrl")
                        .value("http://localhost:8080/google"));
    }

    @Test
    void getAllUrls_ShouldReturnOk() throws Exception {

        AllUrls allUrls = new AllUrls();
        allUrls.setEntriesCount(0);

        Mockito.when(urlService.getAllUrls())
                .thenReturn(allUrls);

        mockMvc.perform(get("/api/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entriesCount").value(0));
    }

    @Test
    void analysis_ShouldReturnStats() throws Exception {

        UrlStats stats = UrlStats.builder()
                .url("https://google.com")
                .shortId("google")
                .shortUrl("http://localhost:8080/google")
                .clickCount(10L)
                .build();

        Mockito.when(urlService.getStats("google"))
                .thenReturn(stats);

        mockMvc.perform(get("/api/google/analysis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortId").value("google"))
                .andExpect(jsonPath("$.clickCount").value(10));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {

        Mockito.when(urlService.delete("google"))
                .thenReturn("google");

        mockMvc.perform(delete("/api/google"))
                .andExpect(status().isNoContent());
    }
}