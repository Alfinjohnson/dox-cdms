package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.ServiceEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Configuration repository
 */
@Repository
public interface ServiceRepository extends ReactiveCrudRepository<ServiceEntity, Long> {

}
