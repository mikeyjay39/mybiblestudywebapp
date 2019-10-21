package com.mybiblestudywebapp.main;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public class BibleStudyResponse {

    private String book;
    private String chapter;
    private List<Map<String, String>> verses;
    private List<Map<String, String>> notes;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public List<Map<String, String>> getVerses() {
        return verses;
    }

    public void setVerses(List<Map<String, String>> verses) {
        this.verses = verses;
    }

    public List<Map<String, String>> getNotes() {
        return notes;
    }

    public void setNotes(List<Map<String, String>> notes) {
        this.notes = notes;
    }
}
