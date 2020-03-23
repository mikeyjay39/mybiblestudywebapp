package com.mybiblestudywebapp.utils.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class Note {

    private long noteId;
    private String noteText;
    private long userId;
    private long bookId;
    private long chapterId;
    private int verseStart;
    private int verseEnd;
    private int ranking;
    private boolean priv;
    private String lang;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return getNoteId() == note.getNoteId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNoteId());
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public int getVerseStart() {
        return verseStart;
    }

    public void setVerseStart(int verseStart) {

        if (verseStart < 0) {
            this.verseStart = 0;
        } else {
            this.verseStart = verseStart;
        }
    }

    public int getVerseEnd() {
        return verseEnd;
    }

    public void setVerseEnd(int verseEnd) {

        if (verseEnd < this.verseStart) {
            this.verseEnd = verseStart >= 0 ? verseStart : 0;
        } else {
            this.verseEnd = verseEnd;
        }
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
