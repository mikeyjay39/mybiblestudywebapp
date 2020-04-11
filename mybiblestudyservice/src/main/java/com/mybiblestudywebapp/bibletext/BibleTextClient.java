package com.mybiblestudywebapp.bibletext;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/16/20
 */
@FeignClient(contextId = "bibletextClient", name = "zuulservice")
public interface BibleTextClient {

    @GetMapping("/api/bibletext/bibletext/{book}/{chapterNo}")
    List<Map<String, String>> getVerses(@RequestHeader("Authorization") String authHeader,
            @PathVariable String book,
            @PathVariable int chapterNo);
}
