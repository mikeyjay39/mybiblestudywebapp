package com.mybiblestudywebapp.persistence;

/**
 * Thrown by DaoService when tasks don't complete properly.
 * <p>
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/28/19
 */
public class DaoServiceException extends Exception {

    public DaoServiceException() {
    }

    public DaoServiceException(String message) {
        super(message);
    }

    public DaoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoServiceException(Throwable cause) {
        super(cause);
    }

    public DaoServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
