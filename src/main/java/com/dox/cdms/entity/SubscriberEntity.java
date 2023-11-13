package com.dox.cdms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
@Entity
public class SubscriberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
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
    private String json_dt;

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
