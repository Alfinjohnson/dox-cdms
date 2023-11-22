package com.dox.cdms.service;


import com.dox.cdms.entity.CSDMappingEntity;
import com.dox.cdms.repository.CSDMappingRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;


/**
 * @apiNote DataType Service
 */
@Service
@Slf4j(topic = "DataTypeService")
public class CSDMappingService {
    @Autowired
    private final CSDMappingRepository csdMappingRepository;


    @Getter
    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CSDMappingService.class);

    /**
     * constructor for Autowired classes
     * @param csdMappingRepository  constructor
     */
    public CSDMappingService(CSDMappingRepository csdMappingRepository) {
        this.csdMappingRepository = csdMappingRepository;
    }

    /**
     * Create a new CSD mapping entity for the given subscriber and configuration IDs.
     *
     * @param newSubscriberId     The ID of the newly created subscriber.
     * @param newConfigurationId The ID of the newly created configuration.
     * @return The created CSD mapping entity.
     * @throws RuntimeException if there is an error saving the CSD mapping entity.
     */
    public CSDMappingEntity createCSDMapping(Long newSubscriberId, Long newConfigurationId) {
        try {
            logger.info("newSubscriberId: {}, newConfigurationId: {}", newSubscriberId, newConfigurationId);
            CSDMappingEntity csdMappingEntity = new CSDMappingEntity();
            csdMappingEntity.setConfigurationId(newConfigurationId);
            csdMappingEntity.setSubscriberId(newSubscriberId);
            return csdMappingRepository.save(csdMappingEntity);
        } catch (Exception e) {
            logger.error("Error creating CSD mapping entity", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating CSD mapping entity");
        }
    }


    public  ArrayList<Long> findSubscriberByConfigId(long configId) {
        return csdMappingRepository.findByConfigurationId(configId);
    }

    public ArrayList<Long> findSubscriber(long configId) {
        return csdMappingRepository.findByConfigurationId(configId);
    }

    public ArrayList<Long> findSubscriberByConfigName(Long id) {
        return csdMappingRepository.findByConfigurationId(id);
    }
}
