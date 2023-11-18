package com.dox.cdms.payload.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubscriberResponse {
    private Long id;
    private String name;
    private String description;
    private String dataType;
    private Object value;
}
