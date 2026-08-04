package com.shortener.errors;

public class ShortIdExpired extends RuntimeException {
    public ShortIdExpired(String message) {
        super(message);
    }
}
