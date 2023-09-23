package org.gcdms.gcdmssaas.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreatedConfigurationDataModel {

    private Long id;

    private Long configurationId;

    private Long subscriberId;

    private String dataType;

    private Object value;
}
