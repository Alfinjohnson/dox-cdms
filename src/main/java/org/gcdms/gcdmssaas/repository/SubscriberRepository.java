package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.SubscriberEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Service Repository
 */
@Repository
public interface SubscriberRepository extends ReactiveCrudRepository<SubscriberEntity, Long> {

}
