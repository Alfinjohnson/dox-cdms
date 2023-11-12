package com.dox.cdms.payload.request;

import com.dox.cdms.model.UpdateConfigurationDataModel;
import lombok.*;
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
