package com.dox.cdms.repository;


import com.dox.cdms.entity.SubscriberEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
    SELECT se FROM SubscriberEntity se
    LEFT JOIN CSDMappingEntity ce ON ce.subscriberId = se.id
    LEFT JOIN ConfigurationEntity con ON con.id = ce.configurationId
    WHERE con.name = :configName AND se.name = :subscriberName AND se.enabled =true""")
    SubscriberEntity getSubscriberConfig(@Param("configName") String name, @Param("subscriberName") String subscriber);

}
