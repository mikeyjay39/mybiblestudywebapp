package com.mybiblestudywebapp.bibletextservice.getbible;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
@Component
public class GetBibleImpl implements GetBible {

    private static final Logger logger = LoggerFactory.getLogger(GetBibleImpl.class);

    private RestTemplate restTemplate = new RestTemplate();

    private String apiUrl = "https://getbible.net/json?p=";

    public GetBibleImpl() {

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.valueOf("text/html; charset=UTF-8")));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }


    /**
     * {@inheritDoc}
     *
     * @param book    name of the book
     * @param chapter number of the chapter
     * @return GetBibleResponse
     */
    @Override
    public GetBibleResponse getVersesForChapter(String book, int chapter) {
        ResponseEntity<String> response = null;
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptTypes = new ArrayList<>();
        acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);
        acceptTypes.add(MediaType.TEXT_XML);
        headers.setAccept(acceptTypes);
        headers.add("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/77.0.3865.90 Chrome/77.0.3865.90 Safari/537.36");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        try {
            response =
                    restTemplate.exchange(
                            apiUrl + book + String.valueOf(chapter),
                            HttpMethod.GET,
                            request,
                            String.class);
        } catch (RestClientException e) {
            String errMsg = "error fetching verses for book: " + book + " chapter: " + chapter + e.getMessage();
            logger.error(errMsg);
        }

        String stringResponse = "";

        if (response != null) {
            stringResponse = response.getBody();
        }

        logger.debug(stringResponse);
        stringResponse = stringResponse.replace("(", "").replace(")", "");
        ObjectMapper mapper = new ObjectMapper();
        GetBibleChapterResponseImpl objectResponse = null;

        try {
            objectResponse = mapper.readValue(stringResponse, GetBibleChapterResponseImpl.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return objectResponse;
    }
}
