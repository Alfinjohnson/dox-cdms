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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Mono;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

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

    public Mono<CreateConfigurationResponse> createConfiguration(CreateConfigurationRequest createConfigurationRequest) {
        return validateEntityDoesNotExist(createConfigurationRequest.getName())
                .then(saveConfigurationEntityMono(createConfigurationRequest))
                .flatMap(configEntity -> {
                    List<CreatedConfigurationDataModel> createdConfigurationDataModelList = new ArrayList<>();

                    return Flux.fromIterable(createConfigurationRequest.getSubscribers())
                            .flatMap(subscribers -> {
                                final String subName = subscribers.getName();
                                final String subType = subscribers.getType();

                                if ("boolean".equals(subType)) {
                                    final boolean subValue = (boolean) subscribers.getValue();
                                    return saveToBooleanDataEntity(configEntity, subName, subValue)
                                            .doOnNext(booleanDataEntity -> booleanDataEntityToResponseMapper(booleanDataEntity, createdConfigurationDataModelList));
                                }
                                // Handle other subType cases here
                                return Mono.empty();
                            })
                            .then(Mono.just(configEntity))
                            .map(entity -> CreateConfigurationResponse.builder()
                                    .id(entity.getId())
                                    .name(entity.getName())
                                    .subscribers(createdConfigurationDataModelList)
                                    .createdUserId(-1L)
                                    .lastModifiedUserId(-1L)
                                    .build());
                })
                .onErrorMap(CustomException.class, ex -> {
                    log.error("Error occurred during createConfigurationMethod: {}", ex.getMessage(), ex);
                    return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to complete create the response Create Configuration", ex);
                });
    }

    private Mono<Void> validateEntityDoesNotExist(String name) {
        return configurationRepository.findByName(name)
                .flatMap(entity -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity already exists name: " + name)))
                .then();
    }

    private Mono<ConfigurationEntity> saveConfigurationEntityMono(CreateConfigurationRequest createConfigurationRequest) {
        ConfigurationEntity configEntity = new ConfigurationEntity();
        configEntity.setName(createConfigurationRequest.getName());
        configEntity.setDescription(createConfigurationRequest.getDescription());
        configEntity.setCreatedUserId(-1L);
        configEntity.setLastModifiedUserId(-1L);

        return configurationRepository.save(configEntity);
    }

    private Mono<BooleanDataEntity> saveToBooleanDataEntity(ConfigurationEntity configEntity, String subName, boolean subValue) {
        return subscriberService.findIdByName(subName)
                .flatMap(subscriberId -> {
                    BooleanDataEntity booleanDataEntity = new BooleanDataEntity();
                    booleanDataEntity.setConfigurationId(configEntity.getId());
                    booleanDataEntity.setSubscriberId(subscriberId);
                    booleanDataEntity.setDataType("boolean");
                    booleanDataEntity.setCreatedUserId(-1L);
                    booleanDataEntity.setLastModifiedUserId(-1L);
                    booleanDataEntity.setValue(subValue);
                    return booleanDataRepository.save(booleanDataEntity);
                });
    }

    private void booleanDataEntityToResponseMapper(BooleanDataEntity booleanDataEntity, List<CreatedConfigurationDataModel> createdConfigurationDataModelList) {
        CreatedConfigurationDataModel createdConfigurationDataModel = new CreatedConfigurationDataModel();
        createdConfigurationDataModel.setId(booleanDataEntity.getId());
        createdConfigurationDataModel.setConfigurationId(booleanDataEntity.getConfigurationId());
        createdConfigurationDataModel.setValue(booleanDataEntity.getValue());
        createdConfigurationDataModel.setSubscriberId(booleanDataEntity.getSubscriberId());
        createdConfigurationDataModel.setDataType(booleanDataEntity.getDataType());
        createdConfigurationDataModelList.add(createdConfigurationDataModel);
    }
}
