package com.mybiblestudywebapp.bibletext;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
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
        return bibleTextClient.getVerses(book, chapterNo);
    }

    private List<Map<String, String>> getVersesFallback(String book, int chapterNo) {
        Map<String, String> verse = new HashMap<>();
        verse.put("verse", "Error loading verses for " + book + " " + chapterNo);
        verse.put("verseNr", "");
        return List.of(verse);
    }
}