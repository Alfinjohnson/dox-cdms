package org.gcdms.gcdmssaas.payload.request;

import lombok.*;
import org.gcdms.gcdmssaas.model.ConfigurationDataModel;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateConfigurationRequest {

    private String configuration_name;

    private ConfigurationDataModel<?> data;
}
