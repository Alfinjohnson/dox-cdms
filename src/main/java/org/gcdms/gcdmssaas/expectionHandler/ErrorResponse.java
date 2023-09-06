package org.gcdms.gcdmssaas.expectionHandler;

import lombok.Getter;


public record ErrorResponse(int status, String message, String timestamp) {
}
