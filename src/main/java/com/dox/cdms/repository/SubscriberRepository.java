package com.dox.cdms.repository;


import com.dox.cdms.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Service Repository
 */
@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {

    SubscriberEntity findByName(String name);
}
