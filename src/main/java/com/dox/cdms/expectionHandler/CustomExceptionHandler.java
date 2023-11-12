package com.dox.cdms.expectionHandler;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import static com.dox.cdms.utility.AppConst.getCurrentTime;

/**
 * CustomExceptionHandler class
 */
@ControllerAdvice
@Order(-2) // Set an order if needed to control exception handler precedence
public class CustomExceptionHandler {

    @ExceptionHandler(org.gcdms.gcdmssaas.expectionHandler.CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(ServerWebExchange exchange, @NotNull org.gcdms.gcdmssaas.expectionHandler.CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus().value(),
                ex.getMessage(),
                getCurrentTime()
        );
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}
