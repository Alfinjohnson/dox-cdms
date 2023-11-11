package org.gcdms.gcdmssaas.payload.request;

import lombok.*;
import org.gcdms.gcdmssaas.model.UpdateConfigurationDataModel;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateConfigurationRequest {

    private String name;

    private String description;

    private List<UpdateConfigurationDataModel> subscribers;
}
