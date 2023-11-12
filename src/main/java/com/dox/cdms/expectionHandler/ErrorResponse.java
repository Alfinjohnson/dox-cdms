package com.dox.cdms.expectionHandler;

/**
 * Error response model
 * @author alfin
 * @param status
 * @param message
 * @param timestamp
 */
public record ErrorResponse(int status, String message, String timestamp) {
}