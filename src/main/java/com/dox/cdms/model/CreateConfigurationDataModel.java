package com.dox.cdms.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateConfigurationDataModel {

    private String name;

    private String description;

    private String type;

    private Object value;
}
