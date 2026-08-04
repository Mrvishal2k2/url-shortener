package com.shortener.errors;

public class ShortIdNotFound extends Throwable {
    public ShortIdNotFound(String shortIdNotFound) {
        super("Short ID Not Found: " + shortIdNotFound);
    }
}
