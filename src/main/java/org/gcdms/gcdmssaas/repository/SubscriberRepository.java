package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.SubscriberEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Service Repository
 */
@Repository
public interface SubscriberRepository extends ReactiveCrudRepository<SubscriberEntity, Long> {

    Mono<SubscriberEntity> findByName(String name);
}
