package com.dox.cdms.repository;

import com.dox.cdms.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Configuration repository
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    ConfigurationEntity findByName(String name);
}
