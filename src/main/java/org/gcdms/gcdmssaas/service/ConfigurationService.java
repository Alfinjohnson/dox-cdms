package org.gcdms.gcdmssaas.service;

import org.gcdms.gcdmssaas.entity.CSDMappingEntity;
import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.SubscriberEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.model.CreateConfigurationDataModel;
import org.gcdms.gcdmssaas.model.CreatedConfigurationDataModel;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.payload.request.UpdateConfigurationRequest;
import org.gcdms.gcdmssaas.payload.response.CreateConfigurationResponse;
import org.gcdms.gcdmssaas.repository.ConfigurationRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public Mono<CreateConfigurationResponse> createConfiguration(CreateConfigurationRequest createConfigurationRequest) {
        log.info("Creating a new configuration...");
        return validateEntityDoesNotExist(createConfigurationRequest.getName())
                .then(saveConfigurationEntity(createConfigurationRequest))
                .flatMap(savedConfigurationEntity -> {
                    List<CreatedConfigurationDataModel> createdConfigurationDataModels = new ArrayList<>();
                    return Flux.fromIterable(createConfigurationRequest.getSubscribers())
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

    private Mono<CreatedConfigurationDataModel> saveSubscriberAndMapping(ConfigurationEntity savedConfigurationEntity, CreateConfigurationDataModel subscriberRequest, List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        return saveSubscriberEntity(subscriberRequest)
                .flatMap(savedSubscriberEntity -> saveCSDMappingEntity(savedSubscriberEntity.getId(), savedConfigurationEntity.getId())
                        .thenReturn(subscriberMapper(savedSubscriberEntity, createdConfigurationDataModels)));
    }


    private @NotNull Mono<SubscriberEntity> saveSubscriberEntity(@NotNull CreateConfigurationDataModel subscriberRequest) {
        return  saveSubscriberEntity(subscriberRequest.getName(), subscriberRequest.getDescription(), subscriberRequest.getType(), subscriberRequest.getValue());
    }


    private CreateConfigurationResponse createResponse(ConfigurationEntity configurationEntity, List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        log.debug("Creating a response object...");
        return CreateConfigurationResponse.builder()
                .id(configurationEntity.getId())
                .name(configurationEntity.getName())
                .description(configurationEntity.getDescription())
                .subscribers(createdConfigurationDataModels)
                .createdDateTime(configurationEntity.getCreatedDateTime())
                .build();
    }


    private Mono<CSDMappingEntity> saveCSDMappingEntity(Long newSubscriberId, Long newConfigurationId) {
        return csdMappingService.createCSDMapping(newSubscriberId,newConfigurationId);
    }

    private @NotNull Mono<Void> validateEntityDoesNotExist(String name) {
        return configurationRepository.findByName(name)
                .flatMap(entity -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration already exists with name: " + name)))
                .doOnSuccess(entity -> log.debug("Configuration with name {} already exists", name))
                .doOnError(ex -> log.debug("Configuration with name {} does not exist", name))
                .then();
    }

    private @NotNull Mono<ConfigurationEntity> saveConfigurationEntity(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.debug("Saving a new configuration entity...");
        ConfigurationEntity configurationEntity = new ConfigurationEntity();
        configurationEntity.setName(createConfigurationRequest.getName());
        configurationEntity.setDescription(createConfigurationRequest.getDescription());
        return configurationRepository.save(configurationEntity)
                .doOnSuccess(entity -> log.debug("Configuration entity saved with ID: {}", entity.getId()));
    }

    private @NotNull Mono<SubscriberEntity> saveSubscriberEntity( String subscriberName,String subscriberDescription,String subscriberType,Object subscriberValue) {
        log.debug("Saving a new boolean data entity...");
        if ("boolean".equals(subscriberType)) {
            final boolean subscriberValue1 = (boolean) subscriberValue;
            return subscriberService.createNewSubscriber( subscriberName, subscriberDescription, subscriberType, subscriberValue1);
        }
        return Mono.empty();
    }

    private CreatedConfigurationDataModel subscriberMapper(@NotNull SubscriberEntity subscriberEntity, List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
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
    public Mono<CreateConfigurationResponse> updateConfiguration(@NotNull UpdateConfigurationRequest updateConfigurationRequest) {
        // Create a ModelMapper instance
        ModelMapper modelMapper = new ModelMapper();
        // Perform the mapping
        CreateConfigurationRequest createRequest = modelMapper.map(updateConfigurationRequest, CreateConfigurationRequest.class);
        log.info("updateConfiguration");
        return validateEntityDoesNotExist(updateConfigurationRequest.getName())
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
