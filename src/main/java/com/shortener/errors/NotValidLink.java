package com.shortener.errors;

public class NotValidLink extends RuntimeException {
    public NotValidLink(String message) {
        super(message);
    }
}
