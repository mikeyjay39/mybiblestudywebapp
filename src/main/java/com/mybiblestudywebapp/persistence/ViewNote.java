package com.mybiblestudywebapp.persistence;

import java.util.Objects;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class ViewNote {
    private int viewNoteId;
    private int viewId;
    private int noteId;

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

    public int getViewNoteId() {
        return viewNoteId;
    }

    public void setViewNoteId(int viewNoteId) {
        this.viewNoteId = viewNoteId;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
