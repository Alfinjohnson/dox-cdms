package com.dox.cdms.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreatedConfigurationDataModel {

    private Long id;

    private String name;

    private String description;

    private String dataType;

    private boolean enabled;

    private Object value;
}
