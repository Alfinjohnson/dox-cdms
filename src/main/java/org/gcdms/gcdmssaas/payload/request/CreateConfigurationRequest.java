package org.gcdms.gcdmssaas.payload.request;

import lombok.*;
import org.gcdms.gcdmssaas.model.CreateConfigurationDataModel;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateConfigurationRequest {

    private String name;

    private String description;

    private boolean createConfigIfNotFoundEnabled;

    private List<CreateConfigurationDataModel> subscribers;

}
