package com.mybiblestudywebapp.utils.http;

import com.mybiblestudywebapp.utils.http.ErrorResponse;
import com.mybiblestudywebapp.utils.http.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/29/19
 */
public class RankNoteResponse implements Response {

    private ErrorResponse errorResponse;

    /**
     * Contains "success" if successful otherwise a message about the problem
     */
    private String result;

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public RankNoteResponse() {
        this.errorResponse = new ErrorResponse();
    }
}
