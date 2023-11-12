package com.dox.cdms.payload.request;

import com.dox.cdms.model.CreateConfigurationDataModel;
import lombok.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateConfigurationRequest {

    private String name;

    private String description;

    private List<CreateConfigurationDataModel> subscribers;

}
