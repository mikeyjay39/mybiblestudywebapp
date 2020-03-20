package com.mybiblestudywebapp.utils;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/19/20
 */
public enum Constants {
    AUTH_TOKEN("auth-token"),
    CORRELATION_ID("correlation-id"),
    USER_ID("user-id"),
    PRE_FILTER_TYPE("pre"),
    POST_FILTER_TYPE("post"),
    ROUTE_FILTER_TYPE("route");

    private final String name;

    Constants(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
