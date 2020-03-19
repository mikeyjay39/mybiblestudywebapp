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
        CORRELATION_ID("correlation-id"),
        USER_ID("user-id");

        private final String name;

        Constants(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    private String authToken= new String();
    private String correlationId = new String();
    private String userId = new String();

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
