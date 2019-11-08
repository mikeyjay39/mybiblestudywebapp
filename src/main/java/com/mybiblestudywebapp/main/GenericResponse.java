package com.mybiblestudywebapp.main;

/**
 * Use this class as a generic json response for controllers
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/7/19
 */
public class GenericResponse implements Response {

    // TODO - refactor by removing all other response classes that are the same as this class to avoid duplicate code

    private ErrorResponse errorResponse = new ErrorResponse();
    // TODO refactor status name to result
    private String status;
    private long userId;

    @Override
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
