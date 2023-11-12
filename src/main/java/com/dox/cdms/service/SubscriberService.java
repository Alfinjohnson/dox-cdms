package com.dox.cdms.service;


import com.dox.cdms.entity.SubscriberEntity;
import com.dox.cdms.expectionHandler.CustomException;
import com.dox.cdms.payload.request.CreateSubscriberRequest;
import com.dox.cdms.payload.response.CreateSubscriberResponse;
import com.dox.cdms.payload.response.FetchAllSubscriberResponse;
import com.dox.cdms.repository.SubscriberRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


/**
 * @apiNote Subscriber service
 */
@Service
@Slf4j(topic = "SubscriberService")
public class SubscriberService {
    @Autowired
    private final SubscriberRepository subscriberRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @Getter
    @Autowired
    private ModelMapper modelMapper;

    /**
     * constructor for Autowired classes
     * @param subscriberRepository  constructor
     */
    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * Service class for creating new subscription member
     * @param createSubscriberRequest DTO class for create Subscriber Request
     * @return Mono<Long>
     */
    public CreateSubscriberResponse createSubscription(CreateSubscriberRequest createSubscriberRequest) {
        log.info("createSubscription:, {}",createSubscriberRequest);
        return createSubscriptionMethod(createSubscriberRequest);
    }

    /**
     * Method of createSubscription
     * @param createSubscriberRequest DTO class for create Subscriber Request
     * @return Mono<Long>
     */
    private @NotNull CreateSubscriberResponse createSubscriptionMethod(CreateSubscriberRequest createSubscriberRequest ) {
        log.info("createSubscriptionMethod:, {}",createSubscriberRequest);
        return Mono.just(createSubscriberRequest)
                .map(request -> SubscriberEntity.builder()
                        .name(createSubscriberRequest.getName())
                        .description(createSubscriberRequest.getDescription())
                        .build())
                .flatMap(subscriberRepository::save) // Save the SubscriberEntity
                .map(this::createSubscriptionToResponse)
                .onErrorMap(CustomException.class, ex ->
                        new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "createSubscriptionMethod: " +
                                "Failed to complete create Subscription request"));
    }

    private CreateSubscriberResponse createSubscriptionToResponse(SubscriberEntity subscriberEntity) {
        log.info("createSubscriptionToResponse:, {}",subscriberEntity);
        // Use your modelMapper or any mapping logic to map the entity to the response
        return modelMapper.map(subscriberEntity, CreateSubscriberResponse.class);
    }

    /**
     * Service for fetching all the subscribers
     * @return Flux<FetchAllSubscriberResponse>
     */
    public FetchAllSubscriberResponse fetchAllSubscriber() {
        log.info("fetchAllSubscriber: no param");
        return subscriberRepository.findAll()
                .map(this::mapSubscriberToResponse)
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch all subscribers.")
                );
    }

    /**
     * Mapper method for mapping Subscriber entity to DTO class
     * @param subscriberEntity Entity class
     * @return FetchAllSubscriberResponse
     */
    private FetchAllSubscriberResponse mapSubscriberToResponse(SubscriberEntity subscriberEntity) {
        log.info("mapSubscriberToResponse:, {}",subscriberEntity);
        // Use your modelMapper or any mapping logic to map the entity to the response
        return modelMapper.map(subscriberEntity, FetchAllSubscriberResponse.class);
    }

//    public Mono<Long> findIdByName(String name) {
//        logger.info("findIdByName name: {}",name);
//        return subscriberRepository.findByName(name)
//                .map(SubscriberEntity::getId)
//                .switchIfEmpty(Mono.defer(() -> (Mono<Long>) createNewSubscriber(name)))
//                .doOnError(error -> logger.error("Error in findIdByName: {}", error.getMessage(), error));
//    }


    public SubscriberEntity createNewSubscriber(String subscriberName, String subscriberDescription, String subscriberType, Boolean subscriberValue) {
        logger.info("Creating a new subscriber: name={}", subscriberName);

        if (subscriberName == null || subscriberName.isEmpty()) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Subscriber name cannot be null or empty"));
        }

        SubscriberEntity newSubscriber = SubscriberEntity.builder()
                .name(subscriberName)
                .description(subscriberDescription)
                .dataType(subscriberType)
                .boolean_dt(subscriberValue)
                .enabled(true)
                .double_dt(null)
                .json_dt(null)
                .float_dt(null)
                .string_dt(null)
                .integer_dt(null)
                .build();

        return subscriberRepository.save(newSubscriber)
                .doOnError(error -> logger.error("Error in creating new subscriber (name={}): {}", subscriberName, error.getMessage(), error))
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to complete create Subscription request")
                );
    }



}
