package com.dox.cdms.repository;


import com.dox.cdms.entity.SubscriberEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Repository
 */
@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {
    @Transactional
    @Modifying
    @Query(value = """
        update SubscriberEntity s set
        s.name = CASE WHEN :name IS NOT NULL THEN :name ELSE s.name END,
        s.description = CASE WHEN :description IS NOT NULL THEN :description ELSE s.description END,
        s.enabled = CASE WHEN :enabled IS NOT NULL THEN :enabled ELSE s.enabled END,
        s.dataType = CASE WHEN :dataType IS NOT NULL THEN :dataType ELSE s.dataType END,
        s.boolean_dt = CASE WHEN :dataType = 'boolean' AND :value IS NOT NULL THEN CAST(:value AS BOOLEAN) ELSE s.boolean_dt END,
        s.string_dt = CASE WHEN :dataType = 'string' AND :value IS NOT NULL THEN :value ELSE s.string_dt END,
        s.double_dt = CASE WHEN :dataType = 'double' AND :value IS NOT NULL THEN CAST(:value AS DOUBLE) ELSE s.double_dt END,
        s.integer_dt = CASE WHEN :dataType = 'integer' AND :value IS NOT NULL THEN CAST(:value AS INTEGER) ELSE s.integer_dt END,
        s.float_dt = CASE WHEN :dataType = 'float' AND :value IS NOT NULL THEN CAST(:value AS FLOAT) ELSE s.float_dt END,
        s.json_dt = CASE WHEN :dataType = 'json' AND :value IS NOT NULL THEN CAST(:value AS text) ELSE s.json_dt END
        where s.id = :id""")
    int updateNameAndDescriptionAndEnabledAndBoolean_dtAndString_dtAndDouble_dtAndInteger_dtAndFloat_dtAndJson_dtById(
            @Param("name") @Nullable String name,
            @Param("description") @Nullable String description,
            @Param("enabled") @Nullable Boolean enabled,
            @Param("dataType") @Nullable String dataType,
            @Param("value") @Nullable Object value,
            @Param("id") @NonNull Long id);

    @Override
    @NotNull
    Optional<SubscriberEntity> findById(@NotNull Long aLong);
    @Transactional
    void deleteById(@NonNull Long id);

}
