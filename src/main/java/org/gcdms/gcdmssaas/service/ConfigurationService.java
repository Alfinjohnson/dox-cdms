package org.gcdms.gcdmssaas.service;

import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.dataType.BooleanDataEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.model.CreatedConfigurationDataModel;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.payload.response.CreateConfigurationResponse;
import org.gcdms.gcdmssaas.repository.BooleanDataRepository;
import org.gcdms.gcdmssaas.repository.ConfigurationRepository;
import org.jetbrains.annotations.NotNull;
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
    private final BooleanDataRepository booleanDataRepository;
    private final SubscriberService subscriberService;

    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    public ConfigurationService(ConfigurationRepository configurationRepository, BooleanDataRepository booleanDataRepository, SubscriberService subscriberService) {
        this.configurationRepository = configurationRepository;
        this.booleanDataRepository = booleanDataRepository;
        this.subscriberService = subscriberService;
    }

    /**
     * Create a new configuration with subscribers and return a response.
     *
     * @param createConfigurationRequest The request object containing configuration and subscriber data.
     * @return A Mono containing the CreateConfigurationResponse.
     */
    @Transactional
    public Mono<CreateConfigurationResponse> createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.info("Creating a new configuration...");
        return validateEntityDoesNotExist(createConfigurationRequest.getName())
                .then(saveConfigurationEntityMono(createConfigurationRequest))
                .flatMap(savedConfigurationEntity -> {
                    List<CreatedConfigurationDataModel> createdConfigurationDataModels = new ArrayList<>();

                    return Flux.fromIterable(createConfigurationRequest.getSubscribers())
                            .flatMap(subscriberRequest -> {
                                final String subscriberName = subscriberRequest.getName();
                                final String subscriberType = subscriberRequest.getType();

                                if ("boolean".equals(subscriberType)) {
                                    final boolean subscriberValue = (boolean) subscriberRequest.getValue();
                                    return saveBooleanDataEntity(savedConfigurationEntity, subscriberName, subscriberValue)
                                            .doOnNext(savedBooleanDataEntity -> mapToResponse(savedBooleanDataEntity, createdConfigurationDataModels));
                                }
                                // Handle other subscriber types here
                                return Mono.empty();
                            })
                            .then(Mono.just(savedConfigurationEntity))
                            .map(configurationEntity -> createResponse(configurationEntity, createdConfigurationDataModels));
                })
                .doOnSuccess(response -> log.info("Configuration successfully created: {}", response.getName()))
                .doOnError(ex -> log.error("Error occurred during createConfiguration: {}", ex.getMessage(), ex))
                .onErrorMap(CustomException.class, ex -> {
                    log.warn("Failed to create configuration: {}", ex.getMessage(), ex);
                    return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create configuration", ex);
                });
    }

    private @NotNull Mono<Void> validateEntityDoesNotExist(String name) {
        return configurationRepository.findByName(name)
                .flatMap(entity -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration already exists with name: " + name)))
                .doOnSuccess(entity -> log.debug("Configuration with name {} already exists", name))
                .doOnError(ex -> log.debug("Configuration with name {} does not exist", name))
                .then();
    }

    private @NotNull Mono<ConfigurationEntity> saveConfigurationEntityMono(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.debug("Saving a new configuration entity...");
        ConfigurationEntity configurationEntity = new ConfigurationEntity();
        configurationEntity.setName(createConfigurationRequest.getName());
        configurationEntity.setDescription(createConfigurationRequest.getDescription());
        configurationEntity.setCreatedUserId(-1L);
        configurationEntity.setLastModifiedUserId(-1L);

        return configurationRepository.save(configurationEntity)
                .doOnSuccess(entity -> log.debug("Configuration entity saved with ID: {}", entity.getId()));
    }

    private @NotNull Mono<BooleanDataEntity> saveBooleanDataEntity(ConfigurationEntity configurationEntity, String subscriberName, boolean subscriberValue) {
        log.debug("Saving a new boolean data entity...");
        return subscriberService.findIdByName(subscriberName)
                .flatMap(subscriberId -> {
                    BooleanDataEntity booleanDataEntity = new BooleanDataEntity();
                    booleanDataEntity.setConfigurationId(configurationEntity.getId());
                    booleanDataEntity.setSubscriberId(subscriberId);
                    booleanDataEntity.setDataType("boolean");
                    booleanDataEntity.setCreatedUserId(-1L);
                    booleanDataEntity.setLastModifiedUserId(-1L);
                    booleanDataEntity.setValue(subscriberValue);

                    return booleanDataRepository.save(booleanDataEntity)
                            .doOnSuccess(entity -> log.debug("Boolean data entity saved with ID: {}", entity.getId()));
                });
    }

    private void mapToResponse(@NotNull BooleanDataEntity booleanDataEntity, @NotNull List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        CreatedConfigurationDataModel createdConfigurationDataModel = new CreatedConfigurationDataModel();
        createdConfigurationDataModel.setId(booleanDataEntity.getId());
        createdConfigurationDataModel.setConfigurationId(booleanDataEntity.getConfigurationId());
        createdConfigurationDataModel.setValue(booleanDataEntity.getValue());
        createdConfigurationDataModel.setSubscriberId(booleanDataEntity.getSubscriberId());
        createdConfigurationDataModel.setDataType(booleanDataEntity.getDataType());
        createdConfigurationDataModels.add(createdConfigurationDataModel);
    }

    private CreateConfigurationResponse createResponse(@NotNull ConfigurationEntity configurationEntity, List<CreatedConfigurationDataModel> createdConfigurationDataModels) {
        log.debug("Creating a response object...");
        return CreateConfigurationResponse.builder()
                .id(configurationEntity.getId())
                .name(configurationEntity.getName())
                .subscribers(createdConfigurationDataModels)
                .createdUserId(-1L)
                .lastModifiedUserId(-1L)
                .build();
    }
}
