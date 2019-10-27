package com.mybiblestudywebapp.unittests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/25/19
 */
public class BcryptTest {

    private String salt = "thisisasalt";

    @Test
    public void testHash() {
        //String hash = new BCryptPasswordEncoder().encode("12345");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom(salt.getBytes()));
        String hash = encoder.encode("12345");

        // $2a$10$FurPOUtHvvQwUoYw40A.GOIjaG0ThzMzWPJaReFtrxvw8DIQ541.e
        // $2a$10$4qWaGkIcnXv1IxyxbkSBPenVjpS0O11PECJU9KG8I2M1uw.Xx3VyC
        // $2a$12$5giQiMIatImgkk7Slf2TIOOt1azBgWjCA3Rsp6JQ5JRDLrYF14unS
        // $2a$12$.fOkjSnIcUV8piAAbqQzUO4SHr6NY2KHvp6Ljl10TP9mGPeC022.y
        Assert.assertTrue(encoder.matches("12345", hash));

    }
}
