package com.dox.cdms.payload.request;

import lombok.*;


/**
 * payload request class to create new subscriber
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class CreateSubscriberRequest {

    private String name;

    private String description;
}
