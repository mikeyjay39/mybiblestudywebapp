package com.mybiblestudywebapp.utils.persistence.model;


import java.util.Objects;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class ViewNote {

    private long viewNoteId;
    private long viewId;
    private long noteId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewNote viewNote = (ViewNote) o;
        return getViewNoteId() == viewNote.getViewNoteId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getViewNoteId());
    }

    public long getViewNoteId() {
        return viewNoteId;
    }

    public void setViewNoteId(long viewNoteId) {
        this.viewNoteId = viewNoteId;
    }

    public long getViewId() {
        return viewId;
    }

    public void setViewId(long viewId) {
        this.viewId = viewId;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
}
