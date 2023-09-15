package org.gcdms.gcdmssaas.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity for subscriber
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

}
