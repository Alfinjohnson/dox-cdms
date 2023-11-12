package com.dox.cdms.repository;


import com.dox.cdms.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DataType repository
 */
@Repository
public interface BooleanDataRepository extends JpaRepository<SubscriberEntity, Long> {

}
