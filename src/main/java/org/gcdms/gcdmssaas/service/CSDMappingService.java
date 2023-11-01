package org.gcdms.gcdmssaas.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.entity.CSDMappingEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.payload.request.CreateDataTypeRequest;
import org.gcdms.gcdmssaas.payload.response.CreateDataTypeResponse;
import org.gcdms.gcdmssaas.payload.response.FetchAllDataTypeResponse;
import org.gcdms.gcdmssaas.repository.CSDMappingRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @apiNote DataType Service
 */
@Service
@Slf4j(topic = "DataTypeService")
public class CSDMappingService {
    @Autowired
    private final CSDMappingRepository csdMappingRepository;


    @Getter
    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CSDMappingService.class);

    /**
     * constructor for Autowired classes
     * @param csdMappingRepository  constructor
     */
    public CSDMappingService(CSDMappingRepository csdMappingRepository) {
        this.csdMappingRepository = csdMappingRepository;
    }



    /**
     * Service class for creating new subscription member
     * @param createDataTypeRequest DTO class for create DataType Request
     * @return Mono<Long>
     */

    public Mono<CreateDataTypeResponse> createCSDMapping(CreateDataTypeRequest createDataTypeRequest) {
        log.info("createDataTypeRequest: {}",createDataTypeRequest);
        return createDataTypeMethod(createDataTypeRequest);
    }
    /**
     * Method of createSubscription
     * @param createDataTypeRequest DTO class for create Subscriber Request
     * @return Mono<Long>
     */
    private @NotNull Mono<CreateDataTypeResponse> createDataTypeMethod(CreateDataTypeRequest createDataTypeRequest ) {
        log.info("createDataTypeMethod:, {}",createDataTypeRequest);
        return Mono.just(createDataTypeRequest)
                .map(request -> CSDMappingEntity.builder()
                        //.type(createDataTypeRequest.getType())
                        .build())
                .flatMap(csdMappingRepository::save) // Save the SubscriberEntity
                .map(this::createDataTypeToResponse)
                .onErrorMap(CustomException.class, ex ->
                        new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "createSubscriptionMethod: " +
                                "Failed to complete create the request createDataTypeMethod"));
    }

    private CreateDataTypeResponse createDataTypeToResponse(CSDMappingEntity dataTypeEntity) {
        log.info("createDataTypeToResponse:, {}",dataTypeEntity);
        // Use your modelMapper or any mapping logic to map the entity to the response
        return modelMapper.map(dataTypeEntity, CreateDataTypeResponse.class);
    }

    /**
     * Service for fetching all the DataTypes
     * @return Flux<FetchAllDataTypeResponse>
     */
    public Flux<FetchAllDataTypeResponse> fetchAllDatatype() {
        log.info("fetchAllSDataType: no param");
        return csdMappingRepository.findAll()
                .map(this::mapSubscriberToResponse)
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch all subscribers.")
                );
    }

    /**
     * Mapper method for mapping Subscriber entity to DTO class
     * @param dataTypeEntity Entity class
     * @return FetchAllSubscriberResponse
     */
    private FetchAllDataTypeResponse mapSubscriberToResponse(CSDMappingEntity dataTypeEntity) {
        log.info("dataTypeEntity:, {}",dataTypeEntity);
        // Use your modelMapper or any mapping logic to map the entity to the response
        return modelMapper.map(dataTypeEntity, FetchAllDataTypeResponse.class);
    }


    public Mono<CSDMappingEntity> createCSDMapping(Long newSubscriberId, Long newConfigurationId) {
        log.info("newSubscriberId: {}, newConfigurationId:, {}",newSubscriberId,newConfigurationId);
        return Mono.just(newSubscriberId)
                .map(request -> CSDMappingEntity.builder()
                        .configurationId(newConfigurationId)
                        .subscriberId(newSubscriberId)
                        .build())
                .flatMap(csdMappingRepository::save)
                .doOnError(error -> logger.error("Error in createCSDMapping: {}", error.getMessage(), error))
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Failed to complete create createCSDMapping request")
                );
    }
}
