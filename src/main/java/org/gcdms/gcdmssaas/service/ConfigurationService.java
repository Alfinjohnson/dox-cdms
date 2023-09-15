package org.gcdms.gcdmssaas.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.repository.ConfigurationRepository;
import org.gcdms.gcdmssaas.repository.DataTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Configuration Service
 */
@Service
@Slf4j(topic = "ConfigurationService")
public class ConfigurationService {

    @Autowired
    private final ConfigurationRepository configurationRepository;

    @Getter
    @Autowired
    private final DataTypeRepository typeConfigRepository;

    @Getter
    @Autowired
    private ModelMapper modelMapper;

    /**
     * constructor for Autowired classes
     * @param configurationRepository constructor
     * @param typeConfigRepository constructor
     * @param modelMapper constructor
     */
    public ConfigurationService(ConfigurationRepository configurationRepository, DataTypeRepository typeConfigRepository, ModelMapper modelMapper) {
        this.configurationRepository = configurationRepository;
        this.typeConfigRepository = typeConfigRepository;
        this.modelMapper = modelMapper;
    }


    /**
     * Service for crating new configuration
     * @return  Long
     */
    public Mono<Long> createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {
        //Mono<Long> typeConfigId = createTypeConfigMethod(configurationId,createConfigurationRequest.getData().getType());
        return createConfigurationMethod(createConfigurationRequest.getConfiguration_name());
    }

    /**
     * Create configuration method
     * @param name name of configuration
     * @return Mono<Long>
     */
    private @NotNull Mono<Long> createConfigurationMethod(String name) {
         return Mono.just(name)
                .map(request -> ConfigurationEntity.builder()
                        .name(name)
                        .createdUserId(-1L)
                        .lastModifiedUserId(-1L)
                        .build())
                .flatMap(configurationRepository::save) // Save the entity
                .map(ConfigurationEntity::getId)
                .onErrorMap(CustomException.class, ex ->
                        new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save configuration"));
    }

}
