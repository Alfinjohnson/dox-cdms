package com.dox.cdms.repository;

import com.dox.cdms.entity.ConfigurationEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Configuration repository
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {
    @Query("select (count(c) > 0) from ConfigurationEntity c where c.name = ?1 and c.enabled = true")
    boolean existEnabledTrue(@NonNull String name);



    @Transactional
    @Modifying
    @Query("update ConfigurationEntity c set c.description = ?2 where c.name = ?1")
    int updateConfigDescriptionByName(String name, String description);

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


    /**
     * deleteByName
     * @param name configuration name
     * @return long
     */
    @Transactional
    long deleteByName(@NonNull String name);

    /**
     * @param name  configuration name
     * @return ConfigurationEntity
     */
    ConfigurationEntity findByName(@NonNull String name);
}
