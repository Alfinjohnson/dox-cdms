package com.dox.cdms.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateConfigurationDataModel {

    private String name;

    private String description;

    private String type;

    private Object value;
}
