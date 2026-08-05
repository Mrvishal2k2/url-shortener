package com.shortener.utils;

import com.shortener.util.ShortenerUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShortenerUtilsTest {

    @InjectMocks
    private ShortenerUtils shortenerUtils;

    @Test
    void generatesUniqueShortId(){

        String generated = shortenerUtils.generateShortId();
        String generated2 = shortenerUtils.generateShortId();

        Assertions.assertNotNull(generated);
        Assertions.assertNotNull(generated2);

        Assertions.assertEquals(7, generated.length());
        Assertions.assertEquals(7, generated2.length());

        Assertions.assertTrue(!generated.equals(generated2));
        Assertions.assertNotEquals(generated, generated2);
    }
}
