package com.dox.cdms.service;


import com.dox.cdms.entity.CSDMappingEntity;
import com.dox.cdms.entity.ConfigurationEntity;
import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.model.CreatedConfigurationDataModel;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.repository.ConfigurationRepository;
import com.dox.cdms.repository.SubscriberRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ModelMapper modelMapper;
    private final SubscriberService subscriberService;
    private final CSDMappingService csdMappingService;
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    public ConfigurationService(ConfigurationRepository configurationRepository, ModelMapper modelMapper, SubscriberService subscriberService, CSDMappingService csdMappingService) {
        this.configurationRepository = configurationRepository;
        this.modelMapper = modelMapper;
        this.csdMappingService = csdMappingService;
        this.subscriberService = subscriberService;
    }

    /**
     * Create a new configuration with subscribers and return a response.
     *
     * @param createConfigurationRequest The request object containing configuration and subscriber data.
     * @return A Mono containing the CreateConfigurationResponse.
     */
    @Transactional
    public CreateConfigurationResponse createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        logger.info("Creating a new configuration...");
        boolean  validateConfigEntityExistError = validateConfigEntityExistByName(createConfigurationRequest.getName());
        if (validateConfigEntityExistError)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration already exists with name: " + createConfigurationRequest.getName());
        ConfigurationEntity createdConfig = createConfigurationEntity(createConfigurationRequest);
        List<CreatedConfigurationDataModel> createdConfigurationDataModelList = new ArrayList<>();

        for (CreateConfigurationDataModel configModel : createConfigurationRequest.getSubscribers()) {
            SubscriberEntity createdSubscriberEntityResponse = createSubscriber(configModel);
            CSDMappingEntity csdMappingResponse = createCSDMappingEntity(createdConfig.getId(), createdSubscriberEntityResponse.getId());
            logger.info("created csdMapping id: {}",csdMappingResponse.getId());
            createdConfigurationDataModelList.add(mapSubscriberToConfigurationDataModel(createdSubscriberEntityResponse));
        }
        return mapConfigDataModelToCreateConfigResponse(createdConfig,createdConfigurationDataModelList);

    }

    public CreateConfigurationResponse mapConfigDataModelToCreateConfigResponse(@NotNull ConfigurationEntity createdConfig, List<CreatedConfigurationDataModel> createdConfigurationDataModelList ) {
        CreateConfigurationResponse createConfigurationResponse = new CreateConfigurationResponse();
        createConfigurationResponse.setId(createdConfig.getId());
        createConfigurationResponse.setName(createdConfig.getName());
        createConfigurationResponse.setDescription(createdConfig.getDescription());
        createConfigurationResponse.setSubscribers(createdConfigurationDataModelList);
        createConfigurationResponse.setCreatedDateTime(createdConfig.getCreatedDateTime());
        return createConfigurationResponse;
    }

    public CreatedConfigurationDataModel mapSubscriberToConfigurationDataModel(SubscriberEntity subscriberEntity) {
        return modelMapper.map(subscriberEntity, CreatedConfigurationDataModel.class);
    }

    private @NotNull SubscriberEntity createSubscriber(CreateConfigurationDataModel configModel) {
        return subscriberService.createSubscriber(configModel);
    }

    private CSDMappingEntity createCSDMappingEntity(Long newSubscriberId, Long newConfigurationId) {
        return csdMappingService.createCSDMapping(newSubscriberId,newConfigurationId);
    }

    /*
    * validate configuration entity exist or not. throw exception if exist
    *
    * */
    private boolean validateConfigEntityExistByName(String name) {
        return configurationRepository.existsByName(name);
    }
    private @NotNull ConfigurationEntity createConfigurationEntity(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        logger.info("Saving a new configuration entity...");
        ConfigurationEntity configurationEntity = new ConfigurationEntity();
        configurationEntity.setName(createConfigurationRequest.getName());
        configurationEntity.setDescription(createConfigurationRequest.getDescription());
        return configurationRepository.save(configurationEntity);
    }
}
