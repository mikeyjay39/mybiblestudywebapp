package com.mybiblestudywebapp.bible;

import com.mybiblestudywebapp.utils.http.ErrorResponse;
import com.mybiblestudywebapp.utils.http.Response;
import com.mybiblestudywebapp.utils.persistence.model.Note;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public class BibleStudyResponse implements Response {

    private String book;

    private int chapter;

    private List<Map<String, String>> verses;

    private List<Note> notes;

    private String errMsg;

    private ErrorResponse errorResponse;

    private long bookId = -1;

    private long chapterId = -1;

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

    public List<Map<String, String>> getVerses() {
        return verses;
    }

    public void setVerses(List<Map<String, String>> verses) {
        this.verses = verses;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
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

    public BibleStudyResponse() {
        this.errorResponse = new ErrorResponse();
    }
}
