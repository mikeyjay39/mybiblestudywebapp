package com.mybiblestudywebapp.integrationtests;

import com.mybiblestudywebapp.main.BibleStudyRequest;
import com.mybiblestudywebapp.main.BibleStudyResponse;
import org.junit.Assert;
import org.junit.Before;
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

    private URI uri;
    private String url;
    private String viewCode = "6e9e6366-f386-11e9-b633-0242ac110002";
    private String book = "Genesis";
    private int chapterNo = 1;

    @Before
    public void setUp() throws Exception {
        url = "http://localhost:" + randomServerPort + "/biblestudy";
        uri = new URI(url);
    }

    @Test
    public void getChapterAndNotes() throws Exception {

        // build request

        BibleStudyRequest bibleStudyRequest = new BibleStudyRequest();
        bibleStudyRequest.setViewCode(viewCode);
        bibleStudyRequest.setBook(book);
        bibleStudyRequest.setChapterNo(chapterNo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptedTypes = new ArrayList<>();
        acceptedTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptedTypes);
        HttpEntity<BibleStudyRequest> request = new HttpEntity<>(bibleStudyRequest, headers);

        // send request
        ResponseEntity<BibleStudyResponse> response = testRestTemplate.exchange(
                uri, HttpMethod.POST, request, BibleStudyResponse.class);

        testResponse(response);
    }

    @Test
    public void testGetRequestChapterAndNotes() throws Exception {
        url = url + "/" + viewCode + "/" + book + "/" + chapterNo;
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptedTypes = new ArrayList<>();
        acceptedTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptedTypes);
        HttpEntity<BibleStudyRequest> request = new HttpEntity<>(null, headers);

        // send request
        ResponseEntity<BibleStudyResponse> response = testRestTemplate.exchange(
                url, HttpMethod.GET, request, BibleStudyResponse.class);

        testResponse(response);
    }

    private void testResponse(ResponseEntity<BibleStudyResponse> response) {
        // test response
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        BibleStudyResponse body = response.getBody();
        Assert.assertTrue(body.getNotes().size() > 0);
    }
}