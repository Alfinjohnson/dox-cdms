package org.gcdms.gcdmssaas.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity for different DataTypes
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "csd_mapping")
@Getter
@Setter
@ToString
public final class CSDMappingEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("configuration_id")
    private Long configurationId;

    @Column("subscriber_id")
    private Long subscriberId;

    @LastModifiedDate
    @Column( "modified_datetime")
    private LocalDateTime modifiedDateTime;

    @CreatedDate
    @Column("created_datetime")
    private LocalDateTime createdDateTime;
}
