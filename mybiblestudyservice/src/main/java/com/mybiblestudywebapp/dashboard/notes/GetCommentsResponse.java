package com.mybiblestudywebapp.dashboard.notes;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.Comment;

import java.util.List;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/8/19
 */
public class GetCommentsResponse implements Response {

    private ErrorResponse errorResponse = new ErrorResponse();
    private List<Comment> comments;

    @Override
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
