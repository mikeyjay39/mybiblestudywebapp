package com.mybiblestudywebapp.getbible;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
@JsonIgnoreProperties(ignoreUnknown =  true)
public class GetBibleChapterResponseImpl implements GetBibleResponse {

    // naming convention broke hear for sake of simplicity in Jackson mappings
    private String type;
    private String version;
    private String book_name;
    private String chapter_nr;
    private String direction;
    private Map<String, Map<String, String>> chapter;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Map<String, String>> getVerses() {
        List<Map<String, String>> verses = new ArrayList<>();
        Set<Integer> verseNumbers = new TreeSet<>();
        Set<String> stringVerseNum = chapter.keySet();

        // generate ordered list of verse numbers
        for (String v : stringVerseNum) {
            verseNumbers.add(Integer.valueOf(v));
        }

        // build map that contains keys of verseNr and verse.
        for (Integer v : verseNumbers) {
            Map<String, String> map = new HashMap<>();
            map.put("verseNr", chapter.get(String.valueOf(v)).get("verse_nr"));
            map.put("verse", chapter.get(String.valueOf(v)).get("verse"));
            verses.add(map);
        }

        return verses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getChapter_nr() {
        return chapter_nr;
    }

    public void setChapter_nr(String chapter_nr) {
        this.chapter_nr = chapter_nr;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Map<String, Map<String, String>> getChapter() {
        return chapter;
    }

    public void setChapter(Map<String, Map<String, String>> chapter) {
        this.chapter = chapter;
    }
}
