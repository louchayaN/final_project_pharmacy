package com.corporation.pharmacy.dao.exception;

import com.corporation.pharmacy.dao.DAOFactory;

/**
 * Thrown when {@link DAOFactory} is not defined for chosen type.
 */

public class UnsupportedStoradgeTypeException extends RuntimeException {

    private static final long serialVersionUID = - 3862687682355132231L;

    public UnsupportedStoradgeTypeException() {
        super();
    }

    public UnsupportedStoradgeTypeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UnsupportedStoradgeTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedStoradgeTypeException(String message) {
        super(message);
    }

    public UnsupportedStoradgeTypeException(Throwable cause) {
        super(cause);
    }

}
