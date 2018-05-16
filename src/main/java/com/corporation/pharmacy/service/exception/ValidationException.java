package com.corporation.pharmacy.service.exception;

public class ValidationException extends Exception {

    private static final long serialVersionUID = - 3618779226956478411L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

}
