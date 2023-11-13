package com.dox.cdms.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateConfigurationRequest {

    private String name;

    private String description;

}
