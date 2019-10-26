package com.mybiblestudywebapp.unittests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/25/19
 */
public class BcryptTest {

    @Test
    public void testHash() {
        String hash = new BCryptPasswordEncoder().encode("12345");

        Assert.assertTrue(new BCryptPasswordEncoder().matches("12345", hash));
    }
}
