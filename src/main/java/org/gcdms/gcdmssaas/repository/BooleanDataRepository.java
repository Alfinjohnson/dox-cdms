package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.SubscriberEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * DataType repository
 */
@Repository
public interface BooleanDataRepository extends ReactiveCrudRepository<SubscriberEntity, Long> {

}
