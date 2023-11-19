package com.dox.cdms.payload.request;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetConfigurationRequest {
    private String name;
    private String subscriber;
}
