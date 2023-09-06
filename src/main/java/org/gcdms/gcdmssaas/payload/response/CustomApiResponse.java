package org.gcdms.gcdmssaas.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @apiNote custom api response class
 * @param <T>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class CustomApiResponse<T> {
    private int statusCode;
    private String message;
    private String timestamp;
    private T data;
}
