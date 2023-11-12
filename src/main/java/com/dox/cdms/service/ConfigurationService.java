package com.dox.cdms.service;


import com.dox.cdms.entity.CSDMappingEntity;
import com.dox.cdms.entity.ConfigurationEntity;
import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.expectionHandler.CustomException;
import com.dox.cdms.model.CreateConfigurationDataModel;
import com.dox.cdms.model.CreatedConfigurationDataModel;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.request.UpdateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.repository.ConfigurationRepository;
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

    private final SubscriberService subscriberService;
    private final CSDMappingService csdMappingService;
    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    public ConfigurationService(ConfigurationRepository configurationRepository, SubscriberService subscriberService, CSDMappingService csdMappingService) {
        this.configurationRepository = configurationRepository;
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
        log.info("Creating a new configuration...");
         validateConfigEntityExistError(createConfigurationRequest.getName());
                saveConfigurationEntity(createConfigurationRequest);

                    List<CreatedConfigurationDataModel> createdConfigurationDataModels = new ArrayList<>();

                      saveSubscriberAndMapping(savedConfigurationEntity, subscriberRequest, createdConfigurationDataModels))
                    return createResponse(savedConfigurationEntity, createdConfigurationDataModels);

    }

    private @NotNull CreatedConfigurationDataModel saveSubscriberAndMapping(ConfigurationEntity savedConfigurationEntity, CreateConfigurationDataModel subscriberRequest, List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        return saveSubscriberEntity(subscriberRequest)
                .flatMap(savedSubscriberEntity -> saveCSDMappingEntity(savedSubscriberEntity.getId(), savedConfigurationEntity.getId())
                        .thenReturn(subscriberMapper(savedSubscriberEntity, createdConfigurationDataModels)));
    }


    private @NotNull SubscriberEntity saveSubscriberEntity(@NotNull CreateConfigurationDataModel subscriberRequest) {
        return  saveSubscriberEntity(subscriberRequest.getName(), subscriberRequest.getDescription(), subscriberRequest.getType(), subscriberRequest.getValue());
    }


    private CreateConfigurationResponse createResponse(@NotNull ConfigurationEntity configurationEntity, List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        log.debug("Creating a response object...");
        return CreateConfigurationResponse.builder()
                .id(configurationEntity.getId())
                .name(configurationEntity.getName())
                .description(configurationEntity.getDescription())
                .subscribers(createdConfigurationDataModels)
                .createdDateTime(configurationEntity.getCreatedDateTime())
                .build();
    }


    private CSDMappingEntity saveCSDMappingEntity(Long newSubscriberId, Long newConfigurationId) {
        return csdMappingService.createCSDMapping(newSubscriberId,newConfigurationId);
    }

    /*
    * validate configuration entity exist or not. throw exception if exist
    * */
    private @NotNull Void validateConfigEntityExistError(String name) {
        return configurationRepository.findByName(name)
                .flatMap(entity -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration already exists with name: " + name)))
                .doOnSuccess(entity -> log.debug("Configuration with name {} already exists", name))
                .doOnError(ex -> log.debug("Configuration with name {} does not exist", name))
                .then();
    }

    /*
     * validate configuration entity exist or not. return entity if exist
     * */
    private @NotNull ConfigurationEntity validateConfigEntityExistReturn(String name) {
        return configurationRepository.findByName(name)
                .doOnSuccess(entity -> log.debug("Configuration with name {} already exists", name))
                .doOnError(entity -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration not exists with name: " + name)))
                .then();
    }

    private @NotNull ConfigurationEntity saveConfigurationEntity(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.debug("Saving a new configuration entity...");
        ConfigurationEntity configurationEntity = new ConfigurationEntity();
        configurationEntity.setName(createConfigurationRequest.getName());
        configurationEntity.setDescription(createConfigurationRequest.getDescription());
        return configurationRepository.save(configurationEntity)
                .doOnSuccess(entity -> log.debug("Configuration entity saved with ID: {}", entity.getId()));
    }

    private @NotNull SubscriberEntity saveSubscriberEntity(String subscriberName, String subscriberDescription, String subscriberType, Object subscriberValue) {
        log.debug("Saving a new boolean data entity...");
        if ("boolean".equals(subscriberType)) {
            final boolean subscriberValue1 = (boolean) subscriberValue;
            return subscriberService.createNewSubscriber( subscriberName, subscriberDescription, subscriberType, subscriberValue1);
        }
        return Mono.empty();
    }

    private @NotNull CreatedConfigurationDataModel subscriberMapper(@NotNull SubscriberEntity subscriberEntity, @NotNull List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        CreatedConfigurationDataModel createdConfigurationDataModel = new CreatedConfigurationDataModel();
        createdConfigurationDataModel.setId(subscriberEntity.getId());
        createdConfigurationDataModel.setName(subscriberEntity.getName());
        createdConfigurationDataModel.setDescription(subscriberEntity.getDescription());
        createdConfigurationDataModel.setValue(subscriberEntity.getBoolean_dt());
        createdConfigurationDataModel.setDataType(subscriberEntity.getDataType());
        createdConfigurationDataModels.add(createdConfigurationDataModel); // Add to the list
        return createdConfigurationDataModel;
    }


//    private Mono<CreateConfigurationResponse> saveConfigurationEntityMono2(CreateConfigurationRequest request) {
//        log.info("Creating a new configuration...");
//        ConfigurationEntity configurationEntity = new ConfigurationEntity();
//        configurationEntity.setName(request.getName());
//        configurationEntity.setDescription(request.getDescription());
//        return configurationRepository.save(configurationEntity)
//                .flatMap(savedEntity -> {
//                    List<CreatedConfigurationDataModel> createdConfigurationDataModels = new ArrayList<>();
//                    return Mono.just(createResponse(savedEntity, createdConfigurationDataModels));
//                })
//                .doOnSuccess(entity -> log.debug("Configuration entity saved with ID: {}", entity.getId()));
//    }




    @Transactional
    public CreateConfigurationResponse updateConfiguration(@NotNull UpdateConfigurationRequest updateConfigurationRequest) {
        // Create a ModelMapper instance
        ModelMapper modelMapper = new ModelMapper();
        // Perform the mapping
        CreateConfigurationRequest createRequest = modelMapper.map(updateConfigurationRequest, CreateConfigurationRequest.class);
        log.info("updateConfiguration");
        return validateConfigEntityExistReturn(createRequest.getName())
                .then(saveConfigurationEntity(createRequest))
                .flatMap(savedConfigurationEntity -> {
                    List<CreatedConfigurationDataModel> createdConfigurationDataModels = new ArrayList<>();
                    return Flux.fromIterable(createRequest.getSubscribers())
                            .flatMap(subscriberRequest -> saveSubscriberAndMapping(savedConfigurationEntity, subscriberRequest, createdConfigurationDataModels))
                            .then(Mono.just(createdConfigurationDataModels)) // Wrap the list in a Mono
                            .map(savedSubscriberEntities -> createResponse(savedConfigurationEntity, createdConfigurationDataModels));
                })
                .doOnSuccess(response -> log.info("Configuration successfully created: {}", response.getName()))
                .doOnError(ex -> log.error("Error occurred during createConfiguration: {}", ex.getMessage(), ex))
                .onErrorMap(CustomException.class, ex -> {
                    log.warn("Failed to create configuration: {}", ex.getMessage(), ex);
                    return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create configuration");
                });
    }


}
