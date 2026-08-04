package com.shortener.errors;

public class ShortIdNotFound extends RuntimeException {
    public ShortIdNotFound(String shortIdNotFound) {
        super("Short ID Not Found: " + shortIdNotFound);
    }
}
