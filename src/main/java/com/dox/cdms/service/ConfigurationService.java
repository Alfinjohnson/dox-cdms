package com.dox.cdms.service;


import com.dox.cdms.entity.CSDMappingEntity;
import com.dox.cdms.entity.ConfigurationEntity;
import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.model.SubscribersDataModel;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.request.DeleteConfigurationRequest;
import com.dox.cdms.payload.request.GetConfigurationRequest;
import com.dox.cdms.payload.request.UpdateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.payload.response.GetConfigurationResponse;
import com.dox.cdms.payload.response.GetFullConfigurationResponse;
import com.dox.cdms.repository.ConfigurationRepository;
import com.dox.cdms.service.imp.ServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.dox.cdms.service.imp.ServiceImp.*;

/**
 * Service class for managing configurations.
 */
@Slf4j(topic = "ConfigurationService")
@Service
public class ConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private CSDMappingService csdMappingService;

    /**
     * Create a new configuration with subscribers and return a response.
     *
     * @param createConfigurationRequest The request object containing configuration and subscriber data.
     * @return A Mono containing the CreateConfigurationResponse.
     * @throws ResponseStatusException if a configuration with the same name already exists.
     */
    @Transactional
    public CreateConfigurationResponse createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        logger.info("Creating a new configuration...");

        if (validateConfigEntityExistByName(createConfigurationRequest.getName().toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration already exists with name: " + createConfigurationRequest.getName());
        }
        ConfigurationEntity createdConfig = createConfigurationEntity(createConfigurationRequest);
        List<SubscribersDataModel> subscribersDataModelList = new ArrayList<>();
        for (CreateConfigurationDataModel configModel : createConfigurationRequest.getSubscribers()) {
            SubscriberEntity createdSubscriberEntityResponse = createSubscriber(configModel);
            CSDMappingEntity csdMappingResponse = createCSDMappingEntity(createdSubscriberEntityResponse.getId(), createdConfig.getId());
            logger.info("created csdMapping id: {}", csdMappingResponse.getId());
            subscribersDataModelList.add(mapSubscriberToConfigurationDataModel(createdSubscriberEntityResponse));
        }

        return mapConfigDataModelToCreateConfigResponse(createdConfig, subscribersDataModelList);
    }

    /**
     * Update configuration.
     *
     * @param updateConfigurationRequest The request object containing configuration update information.
     * @return The number of updated configurations.
     * @throws ResponseStatusException if the configuration with the specified name does not exist.
     */
    @Transactional
    public int updateConfiguration(@NotNull UpdateConfigurationRequest updateConfigurationRequest) {
        logger.info("Updating configuration...");

        if (!validateConfigEntityExistByName(updateConfigurationRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration does not exist with name: " + updateConfigurationRequest.getName());
        }

        return updateConfigurationEntity(updateConfigurationRequest);
    }
    /**
     * Update the description of an existing configuration.
     *
     * @param updateConfigurationRequest The request object containing configuration update information.
     * @return The number of updated configurations.
     */
    @Transactional
    private int updateConfigurationEntity(@NotNull UpdateConfigurationRequest updateConfigurationRequest) {
        return configurationRepository.updateConfigDescriptionByName(updateConfigurationRequest.getName(), updateConfigurationRequest.getDescription());
    }

    /**
     * Create a new subscriber based on the provided configuration model.
     *
     * @param configModel The configuration model containing subscriber data.
     * @return The created subscriber entity.
     */
    private @NotNull SubscriberEntity createSubscriber(CreateConfigurationDataModel configModel) {
        return subscriberService.createSubscriber(configModel);
    }

    /**
     * Create a new CSD mapping entity for the given subscriber and configuration IDs.
     *
     * @param newSubscriberId     The ID of the newly created subscriber.
     * @param newConfigurationId The ID of the newly created configuration.
     * @return The created CSD mapping entity.
     */
    private CSDMappingEntity createCSDMappingEntity(Long newSubscriberId, Long newConfigurationId) {
        return csdMappingService.createCSDMapping(newSubscriberId, newConfigurationId);
    }

    /*
     * Validate whether a configuration entity with the specified name already exists.
     * Throw an exception if it exists.
     *
     * @param name The name of the configuration.
     * @return true if the configuration exists, false otherwise.
     */
    private boolean validateConfigEntityExistByName(String name) {
        return configurationRepository.existsByName(name);
    }

    /**
     * Create a new configuration entity based on the provided request.
     *
     * @param createConfigurationRequest The request object containing configuration data.
     * @return The created configuration entity.
     */
    private @NotNull ConfigurationEntity createConfigurationEntity(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        logger.info("Saving a new configuration entity...");
        ConfigurationEntity configurationEntity = new ConfigurationEntity();
        configurationEntity.setName(createConfigurationRequest.getName().toLowerCase(Locale.ROOT));
        configurationEntity.setDescription(createConfigurationRequest.getDescription());
        configurationEntity.setEnabled(true);
        return configurationRepository.save(configurationEntity);
    }

    /**
     * Delete a configuration by its name.
     *
     * @param deleteConfigurationRequest The request object containing the name of the configuration to delete.
     * @return The number of deleted configurations.
     */
    @Transactional
    public long deleteConfiguration(@NotNull DeleteConfigurationRequest deleteConfigurationRequest) {
        return configurationRepository.deleteByName(deleteConfigurationRequest.getName());
    }

    /**
     * Retrieve the full configuration details, including subscribers, by configuration name.
     *
     * @param configName The name of the configuration.
     * @return The response containing full configuration details.
     */
    public GetFullConfigurationResponse getFullConfiguration(String configName) {
        try {
            ConfigurationEntity configurationEntity = findConfigurationByName(configName);
            List<SubscribersDataModel> subscribersList = new ArrayList<>();
            if ((configurationEntity.getId() == null) || (configurationEntity.getId() == ' ')) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"requested Configuration not found: " + configName);
            ArrayList<Long> subscribersId = csdMappingService.findSubscriberByConfigId(configurationEntity.getId());
            for (Long subscriberId : subscribersId) {
                logger.info("Subscriber ID: " + subscriberId);
                subscribersList.add(subscriberService.getSubscriber(subscriberId));
            }
            return buildGetFullConfigurationResponse(configurationEntity, subscribersList);
        }
        catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"requested Configuration not found: " + configName);
        }
    }

    /**
     * Find a configuration entity by its name.
     *
     * @param name The name of the configuration.
     * @return The configuration entity.
     */
    private ConfigurationEntity findConfigurationByName(String name) {
            return configurationRepository.findByName(name);
    }

    /**
     * Get configuration details based on the request.
     *
     * @param getConfigurationRequest The request object containing configuration details.
     * @return The response containing configuration details.
     */
    public GetConfigurationResponse getConfiguration(@NotNull GetConfigurationRequest getConfigurationRequest) {
        GetConfigurationResponse getConfigurationResponse = new GetConfigurationResponse();
        try {
            if (!configurationRepository.existEnabledTrue(getConfigurationRequest.getName())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "requested configuration disabled: " + getConfigurationRequest.getName());
        }catch ( Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"requested Configuration not found: " + getConfigurationRequest.getName());
        }

        SubscriberEntity subscriberEntity = subscriberService.getSubscriberConfig(getConfigurationRequest.getName(), getConfigurationRequest.getSubscriber());
        logger.info("getConfiguration :{}",subscriberEntity);
        getConfigurationResponse.setName(getConfigurationRequest.getName());
        getConfigurationResponse.setSubscriber(subscriberEntity.getName());
        getConfigurationResponse.setDataType(subscriberEntity.getDataType());
        getConfigurationResponse.setValue(ServiceImp.getDTValueMethod(subscriberEntity));
        return getConfigurationResponse;
    }
}

