package org.gcdms.gcdmssaas.payload.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * payload response class to get all the subscriber
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class FetchAllSubscriberResponse {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime modifiedDateTime;

    private LocalDateTime createdDateTime;

    private Long createdUserId;

    private Long lastModifiedUserId;
}
