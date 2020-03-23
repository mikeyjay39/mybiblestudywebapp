package com.mybiblestudywebapp.dashboard.notes;

import com.mybiblestudywebapp.utils.http.ErrorResponse;
import com.mybiblestudywebapp.utils.http.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/29/19
 */
public class AddNoteResponse implements Response {

    private ErrorResponse errorResponse;

    private long noteId;

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public AddNoteResponse() {
        this.errorResponse = new ErrorResponse();
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
}
