package org.gcdms.gcdmssaas.expectionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom Exception class created to handle different runtime exceptions
 */
public class CustomException extends RuntimeException {
    @Getter
    private final HttpStatus status;
    private final String message;

    @Getter
    private final Throwable ex;

    public CustomException(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        this.ex = ex;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
