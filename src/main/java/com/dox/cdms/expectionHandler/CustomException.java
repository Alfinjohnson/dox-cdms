package com.dox.cdms.expectionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom Exception class created to handle different runtime exceptions
 */
@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;

    private final String message;

    public CustomException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;

    }
}
