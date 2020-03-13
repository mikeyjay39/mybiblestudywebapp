package com.mybiblestudywebapp.main;

/**
 * Holds REST API error messages
 * <p>
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
public class ErrorResponse {

    private String type;

    private String title;

    private int status;

    private String detail;

    private String instance;

    public String getType() {
        return type;
    }

    public ErrorResponse setType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ErrorResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ErrorResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public ErrorResponse setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public ErrorResponse setInstance(String instance) {
        this.instance = instance;
        return this;
    }

}
