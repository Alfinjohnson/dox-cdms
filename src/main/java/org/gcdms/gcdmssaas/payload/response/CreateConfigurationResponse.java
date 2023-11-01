package org.gcdms.gcdmssaas.payload.response;

import lombok.*;
import org.gcdms.gcdmssaas.model.CreateConfigurationDataModel;
import org.gcdms.gcdmssaas.model.CreatedConfigurationDataModel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateConfigurationResponse {

    private Long id;

    private String name;

    private String description;

    private List<CreatedConfigurationDataModel> subscribers;

    private LocalDateTime modifiedDateTime;

    private LocalDateTime createdDateTime;

}
