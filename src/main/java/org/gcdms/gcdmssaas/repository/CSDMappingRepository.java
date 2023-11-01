package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.CSDMappingEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * CSDMapping repository
 */
@Repository
public interface CSDMappingRepository extends ReactiveCrudRepository<CSDMappingEntity, Long> {

}
