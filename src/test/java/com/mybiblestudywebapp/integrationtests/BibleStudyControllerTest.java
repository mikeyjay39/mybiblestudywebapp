package com.mybiblestudywebapp.integrationtests;

import com.mybiblestudywebapp.main.BibleStudyRequest;
import com.mybiblestudywebapp.main.BibleStudyResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/23/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BibleStudyControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void getChapterAndNotes() throws Exception {

        // build request
        String url = "http://localhost:" + randomServerPort + "/biblestudy";
        URI uri = new URI(url);
        BibleStudyRequest bibleStudyRequest = new BibleStudyRequest();
        bibleStudyRequest.setViewCode("6e9e6366-f386-11e9-b633-0242ac110002");
        bibleStudyRequest.setBook("Genesis");
        bibleStudyRequest.setChapterNo(1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptedTypes = new ArrayList<>();
        acceptedTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptedTypes);
        HttpEntity<BibleStudyRequest> request = new HttpEntity<>(bibleStudyRequest, headers);

        // sent request
        ResponseEntity<BibleStudyResponse> response = testRestTemplate.exchange(
                uri, HttpMethod.POST, request, BibleStudyResponse.class);

        // test response
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        BibleStudyResponse body = response.getBody();
        Assert.assertTrue(body.getNotes().size() > 0);
    }
}