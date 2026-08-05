package com.shortener.api;

import com.shortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedirectApi.class)
class RedirectApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Test
    void redirect_ShouldReturn302() throws Exception {

        Mockito.when(urlService.getOriginalUrl("google"))
                .thenReturn("https://google.com");

        mockMvc.perform(get("/google"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://google.com"));

        Mockito.verify(urlService).getOriginalUrl("google");
    }
}