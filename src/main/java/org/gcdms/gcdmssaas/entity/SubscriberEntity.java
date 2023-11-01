package org.gcdms.gcdmssaas.entity;

import lombok.*;
import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.CSDMappingEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity for boolean data type
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscriber")
@Getter
@Setter
@ToString
public final class SubscriberEntity {
    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("data_type")
    private String dataType;

    @Column("enabled")
    private boolean enabled;

    @Column("boolean_dt")
    private Boolean boolean_dt;

    @Column("string_dt")
    private String string_dt;

    @Column("double_dt")
    private Double double_dt;

    @Column("integer_dt")
    private Integer integer_dt;

    @Column("float_dt")
    private Float float_dt;

    @Column("json_dt")
    private Object json_dt;

    @LastModifiedDate
    @Column( "modified_datetime")
    private LocalDateTime modifiedDateTime;

    @CreatedDate
    @Column("created_datetime")
    private LocalDateTime createdDateTime;
}
