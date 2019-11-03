package com.mybiblestudywebapp.dashboard.views;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/2/19
 */
public class AddViewResponse implements Response {

    private ErrorResponse errorResponse;
    private long viewId;

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {

    }

    public long getViewId() {
        return viewId;
    }

    public void setViewId(long viewId) {
        this.viewId = viewId;
    }

    public AddViewResponse() {
        this.errorResponse = new ErrorResponse();
    }
}
