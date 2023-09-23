package org.gcdms.gcdmssaas.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.entity.DataTypeEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.payload.request.CreateDataTypeRequest;
import org.gcdms.gcdmssaas.payload.response.CreateDataTypeResponse;
import org.gcdms.gcdmssaas.payload.response.FetchAllDataTypeResponse;
import org.gcdms.gcdmssaas.repository.DataTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
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
public class DataTypeService {
    @Autowired
    private final DataTypeRepository dataTypeRepository;


    @Getter
    @Autowired
    private ModelMapper modelMapper;

    /**
     * constructor for Autowired classes
     * @param dataTypeRepository  constructor
     */
    public DataTypeService(DataTypeRepository dataTypeRepository) {
        this.dataTypeRepository = dataTypeRepository;
    }



    /**
     * Service class for creating new subscription member
     * @param createDataTypeRequest DTO class for create DataType Request
     * @return Mono<Long>
     */

    public Mono<CreateDataTypeResponse> createDataType(CreateDataTypeRequest createDataTypeRequest) {
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
                .map(request -> DataTypeEntity.builder()
                        .type(createDataTypeRequest.getType())
                        .createdUserId(-1L)
                        .lastModifiedUserId(-1L)
                        .build())
                .flatMap(dataTypeRepository::save) // Save the SubscriberEntity
                .map(this::createDataTypeToResponse)
                .onErrorMap(CustomException.class, ex ->
                        new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "createSubscriptionMethod: " +
                                "Failed to complete create the request createDataTypeMethod", ex));
    }

    private CreateDataTypeResponse createDataTypeToResponse(DataTypeEntity dataTypeEntity) {
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
        return dataTypeRepository.findAll()
                .map(this::mapSubscriberToResponse)
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch all subscribers.", ex)
                );
    }

    /**
     * Mapper method for mapping Subscriber entity to DTO class
     * @param dataTypeEntity Entity class
     * @return FetchAllSubscriberResponse
     */
    private FetchAllDataTypeResponse mapSubscriberToResponse(DataTypeEntity dataTypeEntity) {
        log.info("dataTypeEntity:, {}",dataTypeEntity);
        // Use your modelMapper or any mapping logic to map the entity to the response
        return modelMapper.map(dataTypeEntity, FetchAllDataTypeResponse.class);
    }


}
