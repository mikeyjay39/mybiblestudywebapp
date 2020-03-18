package com.mybiblestudywebapp.utils;

import org.springframework.stereotype.Component;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/17/20
 */
@Component
public class UserContext {
    public enum Constants {
        AUTH_TOKEN("auth-token"),
        USER_ID("user-id");

        final String val;

        Constants(String val) {
            this.val = val;
        }
    }

    private String authToken= new String();
    private String userId = new String();

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
