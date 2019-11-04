package com.mybiblestudywebapp.bible;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/4/19
 */
public class GetChapterResponse implements Response {

    private ErrorResponse errorResponse = new ErrorResponse();
    private String book;
    private int chapter;
    private long bookId = -1;
    private long chapterId = -1;
    private List<Map<String, String>> verses;

    @Override
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public List<Map<String, String>> getVerses() {
        return verses;
    }

    public void setVerses(List<Map<String, String>> verses) {
        this.verses = verses;
    }
}
