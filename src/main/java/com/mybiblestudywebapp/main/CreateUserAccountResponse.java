package com.mybiblestudywebapp.main;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
public class CreateUserAccountResponse extends Response {

    private long userId;

    private String email;

    private String firstname;

    private String lastname;

    public long getUserId() {
        return userId;
    }

    public CreateUserAccountResponse setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserAccountResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public CreateUserAccountResponse setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public CreateUserAccountResponse setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
}
