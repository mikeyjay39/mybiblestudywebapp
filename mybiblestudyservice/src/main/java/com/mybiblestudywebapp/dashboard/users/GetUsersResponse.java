package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/5/19
 */
public class GetUsersResponse implements Response {

    private ErrorResponse errorResponse = new ErrorResponse();
    private List<Map<String, Object>> users;

    @Override
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public List<Map<String, Object>> getUsers() {
        return users;
    }

    public void setUsers(List<Map<String, Object>> users) {
        this.users = users;
    }
}
