package com.shortener.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class ShortenerUtils {

    private static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SHORT_ID_LENGTH = 7;

    public String generateShortId() {
        StringBuilder sb = new StringBuilder(SHORT_ID_LENGTH);
        for (int i = 0; i < SHORT_ID_LENGTH; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
