package org.gcdms.gcdmssaas.service;

import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.data.BooleanDataEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.model.CreatedConfigurationDataModel;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.payload.response.CreateConfigurationResponse;
import org.gcdms.gcdmssaas.repository.BooleanDataRepository;
import org.gcdms.gcdmssaas.repository.ConfigurationRepository;
import org.gcdms.gcdmssaas.repository.DataTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConfigurationService {

    @Autowired
    private final ConfigurationRepository configurationRepository;

    @Autowired
    private final BooleanDataRepository booleanDataRepository;

    @Autowired
    private final DataTypeRepository typeConfigRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final SubscriberService subscriberService;

    public ConfigurationService(ConfigurationRepository configurationRepository, BooleanDataRepository booleanDataRepository, DataTypeRepository typeConfigRepository, ModelMapper modelMapper, SubscriberService subscriberService) {
        this.configurationRepository = configurationRepository;
        this.booleanDataRepository = booleanDataRepository;
        this.typeConfigRepository = typeConfigRepository;
        this.modelMapper = modelMapper;
        this.subscriberService = subscriberService;
    }

    public Mono<CreateConfigurationResponse> createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.info("createConfiguration method called.");
        return createConfigurationMethod(createConfigurationRequest);
    }

    @Transactional
    private @NotNull Mono<CreateConfigurationResponse> createConfigurationMethod(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        log.info("createConfigurationMethod called.");

        List<CreatedConfigurationDataModel> createdConfigurationDataModelList = new ArrayList<>();

        Mono<ConfigurationEntity> configEntity = Mono.defer(() -> {
            log.info("Creating ConfigurationEntity.");
            return Mono.just(createConfigurationRequest)
                    .map(request -> ConfigurationEntity.builder()
                            .name(createConfigurationRequest.getName())
                            .description(createConfigurationRequest.getDescription())
                            .createdUserId(-1L)
                            .lastModifiedUserId(-1L)
                            .build())
                    .flatMap(configurationRepository::save)
                    .doOnNext(entity -> log.info("Saved ConfigurationEntity with ID: {}", entity.getId()))
                    .onErrorMap(CustomException.class, ex ->
                            new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save configuration entity", ex));
        });

// Now you can use configEntityMono wherever you need the ConfigurationEntity

        createConfigurationRequest.getSubscribers().forEach(subscribers -> {

            final String subName = subscribers.getName();
            final String subType = subscribers.getType();

            if (subType.equals("boolean")) {
                final boolean subValue = (boolean) subscribers.getValue();
                Mono<BooleanDataEntity> booleanDataEntityMono = saveToBooleanDataEntity(configEntity, subName, subValue);
                booleanDataEntityToResponseMapper(booleanDataEntityMono, createdConfigurationDataModelList);
            }
            // Handle other subType cases here
        });

        return configEntity.map(entity -> {
            log.info("Mapping configuration entity to response.");
            return CreateConfigurationResponse.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .subscribers(createdConfigurationDataModelList)
                    .createdUserId(-1L)
                    .lastModifiedUserId(-1L)
                    .build();
        }).onErrorMap(CustomException.class, ex -> {
            log.error("Error occurred during createConfigurationMethod: {}", ex.getMessage(), ex);
            return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to complete create the response Create Configuration", ex);
        });
    }

    private void booleanDataEntityToResponseMapper(@NotNull Mono<BooleanDataEntity> booleanDataEntityMono, @NotNull List<CreatedConfigurationDataModel> createdConfigurationDataModelList) {
        log.info("booleanDataEntityToResponseMapper called.");
        booleanDataEntityMono.map(data -> {
            log.info("Mapping BooleanDataEntity to response.");
            return CreatedConfigurationDataModel.builder()
                    .id(data.getId())
                    .configurationId(data.getConfigurationId())
                    .value(data.getValue())
                    .subscriberId(data.getSubscriberId())
                    .dataType(data.getDataType())
                    .build();
        }).doOnSuccess(createdConfigurationDataModelList::add).subscribe();
    }

    public @NotNull Mono<BooleanDataEntity> saveToBooleanDataEntity(Mono<ConfigurationEntity> configEntity, String subName, boolean subValue) {
        log.info("saveToBooleanDataEntity called.");
        return Mono.zip(configEntity, subscriberService.findIdByName(subName))
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
}
