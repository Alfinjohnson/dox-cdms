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
            s.name = :name,
            s.description = :description,
            s.enabled = :enabled,
            s.dataType = :dataType,
            s.boolean_dt = CASE WHEN :dataType = 'boolean' THEN :value ELSE s.boolean_dt END,
            s.string_dt = CASE WHEN :dataType = 'string' THEN :value ELSE s.string_dt END,
            s.double_dt = CASE WHEN :dataType = 'double' THEN :value ELSE s.double_dt END,
            s.integer_dt = CASE WHEN :dataType = 'integer' THEN :value ELSE s.integer_dt END,
            s.float_dt = CASE WHEN :dataType = 'float' THEN :value ELSE s.float_dt END,
            s.json_dt = CASE WHEN :dataType = 'json' THEN :value ELSE s.json_dt END
            where s.id = :id""")
    int updateNameAndDescriptionAndEnabledAndBoolean_dtAndString_dtAndDouble_dtAndInteger_dtAndFloat_dtAndJson_dtById(
            @Param("name") @Nullable String name,
            @Param("description") @Nullable String description,
            @Param("enabled") @Nullable boolean enabled,
            @Param("dataType") @Nullable String dataType,
            @Param("value") @Nullable Object value,
            @Param("id") @Nullable Long id);

    @Override
    @NotNull
    Optional<SubscriberEntity> findById(@NotNull Long aLong);
    @Transactional
    void deleteById(@NonNull Long id);

}
