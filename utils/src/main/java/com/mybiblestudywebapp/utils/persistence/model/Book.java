package com.mybiblestudywebapp.utils.persistence.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class Book {

    private long bookId;
    private String title;
    private Testament testament;
    private List<Chapter> chapters = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getBookId() == book.getBookId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId());
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Testament getTestament() {
        return testament;
    }

    public void setTestament(Testament testament) {
        this.testament = testament;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
