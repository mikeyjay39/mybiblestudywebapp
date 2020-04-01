package com.mybiblestudywebapp.bibletext;

import com.mybiblestudywebapp.redis.BibleTextRedisRepository;
import com.mybiblestudywebapp.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class to call the BibleText microservice
 *
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/17/20
 */
@Service
@DefaultProperties(
        threadPoolKey = "bibleTextThreadPool",
        commandProperties=
                {@HystrixProperty(
                        name="execution.isolation.thread.timeoutInMilliseconds",
                        value="5000")})
public class BibleTextService {

    private BibleTextClient bibleTextClient;

    private static Logger logger = LoggerFactory.getLogger(BibleTextService.class);

    @Autowired
    private BibleTextRedisRepository bibleTextRedisRepository;

    @Autowired
    public BibleTextService(BibleTextClient bibleTextClient) {
        this.bibleTextClient = bibleTextClient;
    }

    /**
     * // TODO make another service class that class this in async wrapper
     * @param book
     * @param chapterNo
     * @return
     */
    @HystrixCommand(fallbackMethod = "getVersesFallback")
    public List<Map<String, String>> getVerses(String book, int chapterNo) {
        logger.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        // check Redis
        String key = String.format("%s %d", book, chapterNo);
        List<Map<String, String>> result = bibleTextRedisRepository.findVerses(key);

        if (null != result) {
            logger.debug("{} found in Redis", key);
            return result;
        }

        logger.debug("{} not found in Redis. Calling Bibletextservice", key);
        result = bibleTextClient.getVerses(book, chapterNo);
        bibleTextRedisRepository.saveVerses(key, result);
        return result;
    }

    private List<Map<String, String>> getVersesFallback(String book, int chapterNo) {
        logger.warn("getVersesFallback method called. Bibletextservice may be unreachable.");
        Map<String, String> verse = new HashMap<>();
        verse.put("verse", String.format("Error loading verses for %s %s", book, chapterNo));
        verse.put("verseNr", "");
        return List.of(verse);
    }
}