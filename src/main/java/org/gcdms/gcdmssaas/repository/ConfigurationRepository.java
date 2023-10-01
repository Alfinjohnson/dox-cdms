package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Configuration repository
 */
@Repository
public interface ConfigurationRepository extends ReactiveCrudRepository<ConfigurationEntity, Long> {

    Mono<ConfigurationEntity> findByName(String name);
}
