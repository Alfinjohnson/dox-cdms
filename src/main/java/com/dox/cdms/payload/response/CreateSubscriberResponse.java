package com.dox.cdms.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateSubscriberResponse {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime modifiedDateTime;

    private LocalDateTime createdDateTime;

    private Long createdUserId;

    private Long lastModifiedUserId;
}
