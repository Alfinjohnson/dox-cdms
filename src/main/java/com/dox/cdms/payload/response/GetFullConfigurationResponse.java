package com.dox.cdms.payload.response;

import com.dox.cdms.model.SubscribersDataModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetFullConfigurationResponse {

    private Long id;

    private String name;

    private String description;

    private List<SubscribersDataModel> subscribers;

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;
}
