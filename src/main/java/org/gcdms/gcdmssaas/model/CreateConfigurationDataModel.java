package org.gcdms.gcdmssaas.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateConfigurationDataModel {

    private String name;

    private String type;

    private Object value;
}
