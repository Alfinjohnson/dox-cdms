package com.dox.cdms.repository;


import com.dox.cdms.entity.SubscriberEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
    @Query("""
            update SubscriberEntity s set s.name = ?1, s.description = ?2, s.enabled = ?3, s.boolean_dt = ?4, s.string_dt = ?5, s.double_dt = ?6, s.integer_dt = ?7, s.float_dt = ?8, s.json_dt = ?9
            where s.id = ?10""")
    int updateNameAndDescriptionAndEnabledAndBoolean_dtAndString_dtAndDouble_dtAndInteger_dtAndFloat_dtAndJson_dtById(@Nullable String name, @Nullable String description, @Nullable boolean enabled, @Nullable Boolean boolean_dt, @Nullable String string_dt, @Nullable Double double_dt, @Nullable Integer integer_dt, @Nullable Float float_dt, @Nullable String json_dt, @NonNull Long id);

    @Override
    @NotNull
    Optional<SubscriberEntity> findById(@NotNull Long aLong);
    @Transactional
    void deleteById(@NonNull Long id);

}
