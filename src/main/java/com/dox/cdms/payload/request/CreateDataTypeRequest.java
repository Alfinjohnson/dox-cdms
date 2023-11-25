package com.dox.cdms.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateDataTypeRequest {
    private String type;
}
