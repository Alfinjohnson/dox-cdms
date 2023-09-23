package org.gcdms.gcdmssaas.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public final class saveToSubscriberEntityModel {

    private Long id;

    private String name;

    private String description;
}
