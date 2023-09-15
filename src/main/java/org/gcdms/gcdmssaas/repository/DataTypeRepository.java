package org.gcdms.gcdmssaas.repository;

import org.gcdms.gcdmssaas.entity.DataTypeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * DataType repository
 */
@Repository
public interface DataTypeRepository extends ReactiveCrudRepository<DataTypeEntity, Long> {

}
