package com.maqs.rdbmsevaluation.exceptions;

/**
 * The service layer exceptions.
 *
 * @author maqbool
 */
public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        this(cause.getMessage(), cause);
    }
}
