package org.gcdms.gcdmssaas.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.entity.ConfigurationEntity;
import org.gcdms.gcdmssaas.entity.TypeConfigEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.repository.ConfigurationRepository;
import org.gcdms.gcdmssaas.repository.TypeConfigRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @apiNote employee service
 */
@Service
@Slf4j(topic = "ConfigurationService")
public class ConfigurationService {
    @Autowired
    private final ConfigurationRepository configurationRepository;

    @Getter
    @Autowired
    private final TypeConfigRepository typeConfigRepository;

    @Getter
    @Autowired
    private ModelMapper modelMapper;

    public ConfigurationService(ConfigurationRepository configurationRepository, TypeConfigRepository typeConfigRepository, ModelMapper modelMapper) {
        this.configurationRepository = configurationRepository;
        this.typeConfigRepository = typeConfigRepository;
        this.modelMapper = modelMapper;
    }


    public Mono<Long> createConfiguration(@NotNull CreateConfigurationRequest createConfigurationRequest) {

        Mono<Long> configurationId = createConfigurationMethod(createConfigurationRequest.getName());
        //Mono<Long> typeConfigId = createTypeConfigMethod(configurationId,createConfigurationRequest.getData().getType());

        return configurationId;
    }

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

    public Mono<Long> createTypeConfigMethod(@NotNull Mono<Long> configurationId, String type) {
        return configurationId.flatMap(id -> {
            TypeConfigEntity typeConfigEntity = TypeConfigEntity.builder()
                    .configurationId(id)
                    .type(type)
                    .createdUserId(-1L)
                    .lastModifiedUserId(-1L)
                    .build();
            return typeConfigRepository.save(typeConfigEntity)
                    .map(TypeConfigEntity::getId)
                    .onErrorMap(CustomException.class, ex ->
                            new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save type configuration"));
        });
    }


}
