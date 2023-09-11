package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Configuration repository
 */
@Repository
public interface ConfigurationRepository extends ReactiveCrudRepository<ConfigurationEntity, Long> {

}
