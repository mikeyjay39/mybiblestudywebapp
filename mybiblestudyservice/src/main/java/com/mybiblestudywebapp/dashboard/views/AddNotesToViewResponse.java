package com.mybiblestudywebapp.dashboard.views;

import com.mybiblestudywebapp.utils.http.ErrorResponse;
import com.mybiblestudywebapp.utils.http.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/6/19
 */
public class AddNotesToViewResponse implements Response {

    private ErrorResponse errorResponse = new ErrorResponse();
    private String result;

    @Override
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
