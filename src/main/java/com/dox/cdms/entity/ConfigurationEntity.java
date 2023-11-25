package com.dox.cdms.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity for configuration
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "configuration")
@Getter
@Setter
@ToString
@Entity
public class ConfigurationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("enabled")
    private Boolean enabled;

    @LastModifiedDate
    @Column( "modified_datetime")
    private LocalDateTime modifiedDateTime;

    @CreatedDate
    @Column("created_datetime")
    private LocalDateTime createdDateTime;

    @PrePersist
    protected void onCreate() {
        createdDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDateTime = LocalDateTime.now();
    }
}
