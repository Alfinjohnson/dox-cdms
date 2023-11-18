package com.dox.cdms.payload.request;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubscriberRequest {
    private Long id;
    private String name;
    private String description;
    private boolean enabled;
    private String dataType;
    private Object value;
}
