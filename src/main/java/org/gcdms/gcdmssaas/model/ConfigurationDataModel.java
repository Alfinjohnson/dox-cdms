package org.gcdms.gcdmssaas.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfigurationDataModel<T> {

    private String serviceName;

    private String type;

    private T value;
}
