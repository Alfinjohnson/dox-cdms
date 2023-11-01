package org.gcdms.gcdmssaas.payload.response;

import lombok.*;
import org.gcdms.gcdmssaas.model.CreatedConfigurationDataModel;
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

    private LocalDateTime createdDateTime;

}
