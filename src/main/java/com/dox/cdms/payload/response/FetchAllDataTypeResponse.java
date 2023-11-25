package com.dox.cdms.payload.response;

import lombok.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FetchAllDataTypeResponse {

    private Long id;

    private String type;

    private LocalDateTime modifiedDateTime;

    private LocalDateTime createdDateTime;

    private Long createdUserId;

    private Long lastModifiedUserId;
}
