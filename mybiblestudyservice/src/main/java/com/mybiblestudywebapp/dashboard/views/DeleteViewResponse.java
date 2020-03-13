package com.mybiblestudywebapp.dashboard.views;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/5/19
 */
public class DeleteViewResponse implements Response {

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
