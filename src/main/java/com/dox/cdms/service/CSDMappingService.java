package com.dox.cdms.service;


import com.dox.cdms.entity.CSDMappingEntity;
import com.dox.cdms.repository.CSDMappingRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public CSDMappingEntity createCSDMapping(Long newSubscriberId, Long newConfigurationId) {
        logger.info("newSubscriberId: {}, newConfigurationId:, {}",newSubscriberId, newConfigurationId);
        CSDMappingEntity csdMappingEntity = new CSDMappingEntity();
        csdMappingEntity.setConfigurationId(newConfigurationId);
        csdMappingEntity.setSubscriberId(newSubscriberId);
        return csdMappingRepository.save(csdMappingEntity);
    }

    public  ArrayList<Long> findSubscriberByConfigId(long configId) {
        return csdMappingRepository.findByConfigurationId(configId);
    }

}
