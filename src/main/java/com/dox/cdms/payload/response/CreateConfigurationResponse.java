package com.dox.cdms.payload.response;

import com.dox.cdms.model.SubscribersDataModel;
import lombok.*;
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

    private Boolean enabled;

    private List<SubscribersDataModel> subscribers;

    private LocalDateTime createdDateTime;

}
