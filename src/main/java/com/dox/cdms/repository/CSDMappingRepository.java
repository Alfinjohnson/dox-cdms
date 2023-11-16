package com.dox.cdms.repository;

import com.dox.cdms.entity.CSDMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


/**
 * CSDMapping repository
 */
@Repository
public interface CSDMappingRepository extends JpaRepository<CSDMappingEntity, Long> {
    @Query("select c.subscriberId from CSDMappingEntity c where c.configurationId = ?1")
    ArrayList<Long> findByConfigurationId(@NonNull Long configurationId);
}
