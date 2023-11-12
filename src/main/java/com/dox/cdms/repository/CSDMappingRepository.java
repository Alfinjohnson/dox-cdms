package com.dox.cdms.repository;

import com.dox.cdms.entity.CSDMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * CSDMapping repository
 */
@Repository
public interface CSDMappingRepository extends JpaRepository<CSDMappingEntity, Long> {

}
