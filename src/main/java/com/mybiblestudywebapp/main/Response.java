package com.mybiblestudywebapp.main;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
public abstract class Response {

    private String type;

    private String title;

    private int status;

    private String detail;

    private String instance;

    public String getType() {
        return type;
    }

    public Response setType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Response setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public Response setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public Response setInstance(String instance) {
        this.instance = instance;
        return this;
    }
}
