package com.dox.cdms.payload.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class GetConfigurationResponse {
    private String name;
    private String subscriber;
    private String dataType;
    private Object value;
}
