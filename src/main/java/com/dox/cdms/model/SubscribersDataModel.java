package com.dox.cdms.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SubscribersDataModel {

    private Long id;

    private String name;

    private String description;

    private String dataType;

    private Object value;
}
