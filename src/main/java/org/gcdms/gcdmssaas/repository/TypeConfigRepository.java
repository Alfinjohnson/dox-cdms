package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.TypeConfigEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * TypeConfig repository
 */
@Repository
public interface TypeConfigRepository extends ReactiveCrudRepository<TypeConfigEntity, Long> {

}
