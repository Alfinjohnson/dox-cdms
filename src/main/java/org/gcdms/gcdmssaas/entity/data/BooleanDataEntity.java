package org.gcdms.gcdmssaas.entity.data;

import lombok.*;
import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.DataTypeEntity;
import org.gcdms.gcdmssaas.entity.SubscriberEntity;
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
@Table(name = "boolean_dt")
@Getter
@Setter
@ToString
public final class BooleanDataEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("configuration_id")
    private Long configuration_id;

    @Column("subscriber_id")
    private Long subscriberId;

    @Column("data_type_id")
    private Long dataTypeId;

    @Column("value")
    private boolean value;

    @LastModifiedDate
    @Column( "modified_datetime")
    private LocalDateTime modifiedDateTime;

    @CreatedDate
    @Column("created_datetime")
    private LocalDateTime createdDateTime;

    @Column("created_userid")
    private Long createdUserId;

    @Column("last_modified_userid")
    private Long lastModifiedUserId;

    @Transient
    private ConfigurationEntity configurationEntity;

    @Transient
    private SubscriberEntity subscriberEntity;

    @Transient
    private DataTypeEntity dataTypeEntity;
}
