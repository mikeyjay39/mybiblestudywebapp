package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.ErrorResponse;
import com.mybiblestudywebapp.main.Response;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
public class CreateUserResponse implements Response {

    private long userId;

    private String email;

    private String firstname;

    private String lastname;

    private ErrorResponse errorResponse;

    public long getUserId() {
        return userId;
    }

    public CreateUserResponse setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public CreateUserResponse setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public CreateUserResponse setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
