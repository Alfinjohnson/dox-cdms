package com.dox.cdms.service;


import com.dox.cdms.entity.CSDMappingEntity;
import com.dox.cdms.entity.ConfigurationEntity;
import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.model.SubscribersDataModel;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.request.DeleteConfigurationRequest;
import com.dox.cdms.payload.request.UpdateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.payload.response.GetFullConfigurationResponse;
import com.dox.cdms.repository.ConfigurationRepository;
import com.dox.cdms.service.imp.ServiceImp;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static com.dox.cdms.service.imp.ServiceImp.*;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final SubscriberService subscriberService;
    private final CSDMappingService csdMappingService;

    private final ServiceImp serviceImp;


    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    public ConfigurationService(ConfigurationRepository configurationRepository, SubscriberService subscriberService, CSDMappingService csdMappingService, ServiceImp serviceImp) {
        this.configurationRepository = configurationRepository;
        this.csdMappingService = csdMappingService;
        this.subscriberService = subscriberService;
        this.serviceImp = serviceImp;
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
        List<SubscribersDataModel> subscribersDataModelList = new ArrayList<>();
        for (CreateConfigurationDataModel configModel : createConfigurationRequest.getSubscribers()) {
            SubscriberEntity createdSubscriberEntityResponse = createSubscriber(configModel);
            CSDMappingEntity csdMappingResponse = createCSDMappingEntity(createdSubscriberEntityResponse.getId(),createdConfig.getId());
            logger.info("created csdMapping id: {}",csdMappingResponse.getId());
            subscribersDataModelList.add(mapSubscriberToConfigurationDataModel(createdSubscriberEntityResponse));
        }
        return mapConfigDataModelToCreateConfigResponse(createdConfig, subscribersDataModelList);

    }


    /**
     * update configuration.
     */
    @Transactional
    public int updateConfiguration(@NotNull UpdateConfigurationRequest updateConfigurationRequest) {
        logger.info("update configuration...");
        boolean  validateConfigEntityExistError = validateConfigEntityExistByName(updateConfigurationRequest.getName());
        if (!validateConfigEntityExistError)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration not exists with name: " + updateConfigurationRequest.getName());
        return updateConfigurationEntity(updateConfigurationRequest);
    }

    @Transactional
    private int updateConfigurationEntity(@NotNull UpdateConfigurationRequest updateConfigurationRequest) {
        return configurationRepository.updateConfigDescriptionByName(updateConfigurationRequest.getName(), updateConfigurationRequest.getDescription());
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

    @Transactional
    public long deleteConfiguration(@NotNull DeleteConfigurationRequest deleteConfigurationRequest) {
        return configurationRepository.deleteByName(deleteConfigurationRequest.getName());
    }

    public GetFullConfigurationResponse getFullConfiguration(String configName) {
        ConfigurationEntity configurationEntity = findConfigurationByName(configName);
        List<SubscribersDataModel> subscribersList = new ArrayList<>();
        ArrayList<Long> subscribersId = csdMappingService.findSubscriberByConfigId(configurationEntity.getId());
        for (Long subscriberId : subscribersId) {
            logger.info("Subscriber ID: " + subscriberId);
            subscribersList.add(serviceImp.findSubscribersById(subscriberId));
        }
        return buildGetFullConfigurationResponse(configurationEntity, subscribersList);
    }

    @NotNull
    private static GetFullConfigurationResponse buildGetFullConfigurationResponse(@NotNull ConfigurationEntity configurationEntity, List<SubscribersDataModel> subscribersList) {
        GetFullConfigurationResponse getFullConfigurationResponse = new GetFullConfigurationResponse();
        getFullConfigurationResponse.setId(configurationEntity.getId());
        getFullConfigurationResponse.setName(configurationEntity.getName());
        getFullConfigurationResponse.setDescription(configurationEntity.getDescription());
        getFullConfigurationResponse.setSubscribers(subscribersList);
        getFullConfigurationResponse.setCreatedDateTime(configurationEntity.getCreatedDateTime());
        getFullConfigurationResponse.setModifiedDateTime(configurationEntity.getModifiedDateTime());
        return getFullConfigurationResponse;
    }



    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    private ConfigurationEntity findConfigurationByName(String name){
        return configurationRepository.findByName(name);

    }

}
