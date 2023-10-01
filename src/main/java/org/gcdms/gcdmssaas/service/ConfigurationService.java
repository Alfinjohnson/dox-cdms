package org.gcdms.gcdmssaas.service;

import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.SubscriberEntity;
import org.gcdms.gcdmssaas.entity.dataType.BooleanDataEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.model.CreatedConfigurationDataModel;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.payload.response.CreateConfigurationResponse;
import org.gcdms.gcdmssaas.repository.BooleanDataRepository;
import org.gcdms.gcdmssaas.repository.ConfigurationRepository;
import org.gcdms.gcdmssaas.repository.DataTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final BooleanDataRepository booleanDataRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    private final SubscriberService subscriberService;
    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    @Autowired
    public ConfigurationService(ConfigurationRepository configurationRepository, BooleanDataRepository booleanDataRepository, DataTypeRepository typeConfigRepository, DataTypeRepository dataTypeRepository, ModelMapper modelMapper, SubscriberService subscriberService) {
        this.configurationRepository = configurationRepository;
        this.booleanDataRepository = booleanDataRepository;
        this.subscriberService = subscriberService;
    }

    public Mono<CreateConfigurationResponse> createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.info("createConfiguration method called.");
        validateEntityDoesNotExist(createConfigurationRequest.getName());
        return createConfigurationMethod(createConfigurationRequest);
    }

    @Transactional
    private @NotNull Mono<CreateConfigurationResponse> createConfigurationMethod(CreateConfigurationRequest createConfigurationRequest) {
        log.info("createConfigurationMethod called.");

        List<CreatedConfigurationDataModel> createdConfigurationDataModelList = new ArrayList<>();

        return saveConfigurationEntityMono(createConfigurationRequest)
                .flatMap(configEntity -> {
                    // Use configEntity directly inside the reactive chain
                    return Flux.fromIterable(createConfigurationRequest.getSubscribers())
                            .flatMap(subscribers -> {
                                final String subName = subscribers.getName();
                                final String subType = subscribers.getType();

                                if (subType.equals("boolean")) {
                                    final boolean subValue = (boolean) subscribers.getValue();
                                    return saveToBooleanDataEntity(configEntity, subName, subValue)
                                            .flatMap(booleanDataEntity -> {
                                                booleanDataEntityToResponseMapper(booleanDataEntity, createdConfigurationDataModelList);
                                                return Mono.empty(); // Return an empty Mono here
                                            });
                                }
                                // Handle other subType cases here
                                return Mono.empty(); // If not a boolean, return an empty Mono
                            })
                            .then(Mono.just(configEntity)); // Use configEntity directly
                })
                .flatMap(entity -> {
                    log.info("Mapping configuration entity to response.");
                    CreateConfigurationResponse response = CreateConfigurationResponse.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .subscribers(createdConfigurationDataModelList)
                            .createdUserId(-1L)
                            .lastModifiedUserId(-1L)
                            .build();
                    return Mono.just(response);
                })
                .onErrorMap(CustomException.class, ex -> {
                    log.error("Error occurred during createConfigurationMethod: {}", ex.getMessage(), ex);
                    return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to complete create the response Create Configuration", ex);
                });
    }

    // TODO add validation for finding existing config name
    private @NotNull Mono<ConfigurationEntity> saveConfigurationEntityMono(CreateConfigurationRequest createConfigurationRequest) {
        log.info("Creating saveConfigurationEntityMono.");

        return Mono.just(createConfigurationRequest)
                .map(request -> ConfigurationEntity.builder()
                        .name(createConfigurationRequest.getName())
                        .description(createConfigurationRequest.getDescription())
                        .createdUserId(-1L)
                        .lastModifiedUserId(-1L)
                        .build())
                .flatMap(configurationRepository::save)
                .doOnError(error -> logger.error("Error in saveConfigurationEntityMono: {}", error.getMessage(), error))
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Failed to complete create saveConfigurationEntityMono request", ex)
                );
    }

    private void booleanDataEntityToResponseMapper(@NotNull BooleanDataEntity booleanDataEntity, @NotNull List<CreatedConfigurationDataModel> createdConfigurationDataModelList) {
        log.info("booleanDataEntityToResponseMapper called.");
        CreatedConfigurationDataModel createdConfigurationDataModel = CreatedConfigurationDataModel.builder()
                .id(booleanDataEntity.getId())
                .configurationId(booleanDataEntity.getConfigurationId())
                .value(booleanDataEntity.getValue())
                .subscriberId(booleanDataEntity.getSubscriberId())
                .dataType(booleanDataEntity.getDataType())
                .build();
        createdConfigurationDataModelList.add(createdConfigurationDataModel);
    }

    private @NotNull Mono<BooleanDataEntity> saveToBooleanDataEntity(ConfigurationEntity configEntity, String subName, boolean subValue) {
        log.info("saveToBooleanDataEntity called.");
        return Mono.zip(Mono.just(configEntity), subscriberService.findIdByName(subName))
                .map(tuple -> {
                    log.info("Mapping parameters to BooleanDataEntity.");
                    BooleanDataEntity booleanDataEntity = new BooleanDataEntity();
                    booleanDataEntity.setConfigurationId(tuple.getT1().getId());
                    booleanDataEntity.setSubscriberId(tuple.getT2());
                    booleanDataEntity.setDataType("boolean");
                    booleanDataEntity.setCreatedUserId(-1L);
                    booleanDataEntity.setLastModifiedUserId(-1L);
                    booleanDataEntity.setValue(subValue);
                    return booleanDataEntity;
                }).flatMap(booleanDataRepository::save)
                .doOnNext(dataEntity -> log.info("Saved BooleanDataEntity with ID: {}", dataEntity.getId()))
                .onErrorMap(CustomException.class, ex -> {
                    log.error("Error occurred during saveToBooleanDataEntity: {}", ex.getMessage(), ex);
                    return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save Boolean Data Entity", ex);
                });
    }

    public Mono<Long> findEntityByName(String name) {
        logger.info("Finding ID by name: {}", name);

        return configurationRepository.findByName(name)
                .map(ConfigurationEntity::getId)
                .onErrorResume(NoSuchElementException.class, ex -> {
                    // Log the error and continue the flow
                    logger.error("Entity not found for name {}: {}", name, ex.getMessage(), ex);
                    return Mono.empty();
                });
    }
    public Mono<Void> validateEntityDoesNotExist(String name) {
        return findEntityByName(name)
                .flatMap(c -> {// Entity found, throw an error
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity already exists name: "+name));
                })
                .then(); // Convert to Mono<Void>
    }


}
