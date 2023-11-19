package com.dox.cdms.repository;


import com.dox.cdms.entity.SubscriberEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Repository
 */
@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {

    @Override
    @NotNull
    Optional<SubscriberEntity> findById(@NotNull Long aLong);
    @Transactional
    void deleteById(@NonNull Long id);

}
