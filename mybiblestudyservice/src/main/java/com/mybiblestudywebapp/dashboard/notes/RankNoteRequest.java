package com.mybiblestudywebapp.dashboard.notes;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/29/19
 */
public class RankNoteRequest {

    private long noteId;
    private long userId;
    private boolean increaseRanking;

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isIncreaseRanking() {
        return increaseRanking;
    }

    public void setIncreaseRanking(boolean increaseRanking) {
        this.increaseRanking = increaseRanking;
    }
}
