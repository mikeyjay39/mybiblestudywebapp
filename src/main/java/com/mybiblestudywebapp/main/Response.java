package com.mybiblestudywebapp.main;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/28/19
 */

public interface Response {

    ErrorResponse getErrorResponse();

    void setErrorResponse(ErrorResponse errorResponse);
}
