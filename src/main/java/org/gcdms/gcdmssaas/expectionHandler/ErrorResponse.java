package org.gcdms.gcdmssaas.expectionHandler;

public record ErrorResponse(int status, String message, String timestamp) {
}
