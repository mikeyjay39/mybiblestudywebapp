package com.mybiblestudywebapp.dashboard.views;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;

import java.util.List;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/4/19
 */
public class GetViewsResponse implements Response {

    private ErrorResponse errorResponse = new ErrorResponse();
    private List<String> viewCodes;

    @Override
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public List<String> getViewCodes() {
        return viewCodes;
    }

    public void setViewCodes(List<String> viewCodes) {
        this.viewCodes = viewCodes;
    }
}
