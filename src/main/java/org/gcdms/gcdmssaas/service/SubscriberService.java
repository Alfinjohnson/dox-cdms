package org.gcdms.gcdmssaas.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.entity.SubscriberEntity;
import org.gcdms.gcdmssaas.expectionHandler.CustomException;
import org.gcdms.gcdmssaas.payload.request.CreateSubscriberRequest;
import org.gcdms.gcdmssaas.payload.response.CreateSubscriberResponse;
import org.gcdms.gcdmssaas.payload.response.FetchAllSubscriberResponse;
import org.gcdms.gcdmssaas.repository.SubscriberRepository;
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
    public Mono<CreateSubscriberResponse> createSubscription(CreateSubscriberRequest createSubscriberRequest) {
        log.info("createSubscription:, {}",createSubscriberRequest);
        return createSubscriptionMethod(createSubscriberRequest);
    }

    /**
     * Method of createSubscription
     * @param createSubscriberRequest DTO class for create Subscriber Request
     * @return Mono<Long>
     */
    private @NotNull Mono<CreateSubscriberResponse> createSubscriptionMethod(CreateSubscriberRequest createSubscriberRequest ) {
        log.info("createSubscriptionMethod:, {}",createSubscriberRequest);
        return Mono.just(createSubscriberRequest)
                .map(request -> SubscriberEntity.builder()
                        .name(createSubscriberRequest.getName())
                        .description(createSubscriberRequest.getDescription())
                        .createdUserId(-1L)
                        .lastModifiedUserId(-1L)
                        .build())
                .flatMap(subscriberRepository::save) // Save the SubscriberEntity
                .map(this::createSubscriptionToResponse)
                .onErrorMap(CustomException.class, ex ->
                        new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "createSubscriptionMethod: " +
                                "Failed to complete create Subscription request", ex));
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
    public Flux<FetchAllSubscriberResponse> fetchAllSubscriber() {
        log.info("fetchAllSubscriber: no param");
        return subscriberRepository.findAll()
                .map(this::mapSubscriberToResponse)
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch all subscribers.", ex)
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

    public Mono<Long> findIdByName(String name) {
        logger.info("findIdByName name: {}",name);
        return subscriberRepository.findByName(name)
                .map(SubscriberEntity::getId)
                .switchIfEmpty(Mono.defer(() -> (Mono<Long>) createNewSubscriber(name)))
                .doOnError(error -> logger.error("Error in findIdByName: {}", error.getMessage(), error));
    }


    private @NotNull Mono<Long> createNewSubscriber(String name) {
        logger.info("createNewSubscriber name: {}",name);
        return Mono.just(name)
                .map(request -> SubscriberEntity.builder()
                        .name(name)
                        .description(name)
                        .createdUserId(-1L)
                        .lastModifiedUserId(-1L)
                        .build())
                .flatMap(subscriberRepository::save)
                .map(SubscriberEntity::getId)
                .doOnError(error -> logger.error("Error in createNewSubscriber: {}", error.getMessage(), error))
                .onErrorMap(
                        CustomException.class,
                        ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Failed to complete create Subscription request", ex)
                );
    }

}
