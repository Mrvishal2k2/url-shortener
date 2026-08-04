package com.shortener;

import org.springframework.boot.SpringApplication;

public class TestShortenerApplication {

    public static void main(String[] args) {
        SpringApplication.from(ShortenerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
