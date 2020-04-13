package com.mybiblestudywebapp.integrationtests;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Base64;
import java.util.List;


/**
 * Launch all services externally before running this test
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/9/20
 */
public class ApiTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();

    /**
     * Tests that we can log in, get verses and notes
     * @throws Exception
     */
    @Test
    public void testApi() throws Exception {
        String jsessionid = loginAndGetSessionId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", jsessionid);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response =
                restTemplate.exchange("http://localhost:8080/biblestudy/0/Genesis/1",
                        HttpMethod.GET,
                        entity,
                        String.class);

        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        Assert.assertNotNull(response.getBody());
    }

    /**
     * Helper method
     * @return
     * @throws Exception
     */
    private String loginAndGetSessionId() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuth =
                Base64.getEncoder()
                        .encodeToString(
                                String.format("%s:%s", "admin@admin.com", "12345")
                                        .getBytes());
        headers.add("Authorization", String.format("Basic %s", encodedAuth));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/login", HttpMethod.GET, entity, String.class);

        HttpHeaders responseHeaders = response.getHeaders();
        List<String> cookie = responseHeaders.get(HttpHeaders.SET_COOKIE);
        return cookie.get(1);
    }
}
