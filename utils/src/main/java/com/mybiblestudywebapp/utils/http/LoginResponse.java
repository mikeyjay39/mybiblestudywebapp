package com.mybiblestudywebapp.utils.http;

import com.mybiblestudywebapp.utils.http.ErrorResponse;
import com.mybiblestudywebapp.utils.http.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/31/19
 */
public class LoginResponse implements Response {

    private ErrorResponse errorResponse;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return this.errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
