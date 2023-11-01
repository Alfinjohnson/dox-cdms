package org.gcdms.gcdmssaas.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

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
