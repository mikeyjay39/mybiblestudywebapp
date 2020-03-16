package com.mybiblestudywebapp.bibletextservice.getbible;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/16/20
 */
@RestController
@RequestMapping("bibletext")
public class GetBibleController {

    @Autowired
    private GetBibleService getBibleService;

    @GetMapping("/{book}/{chapterNo}")
    public List<Map<String, String>> getVerses(@PathVariable String book, @PathVariable int chapterNo) {
        return getBibleService.getVersesForChapter(book, chapterNo);
    }
}
