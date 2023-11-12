package com.dox.cdms.repository;

import com.dox.cdms.entity.ConfigurationEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Configuration repository
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    /**
     * Check if a configuration with the given name exists.
     *
     * @param name Name of the configuration
     * @return True if a configuration with the given name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if a configuration with the given id exists.
     *
     * @param id Id of the configuration
     * @return True if a configuration with the given id exists, false otherwise
     */
    @Override
    boolean existsById(@NotNull Long id);
}
