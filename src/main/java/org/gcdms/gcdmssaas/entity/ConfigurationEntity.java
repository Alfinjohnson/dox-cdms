package org.gcdms.gcdmssaas.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "configuration")
@Getter
@Setter
@ToString
public class ConfigurationEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @LastModifiedDate
    @Column( "modified_datetime")
    private Date modifiedDateTime;

    @CreatedDate
    @Column("created_datetime")
    private Date createdDateTime;

    @Column("created_userid")
    private Long createdUserId;


    @Column("last_modified_userid")
    private Long lastModifiedUserId;
}
