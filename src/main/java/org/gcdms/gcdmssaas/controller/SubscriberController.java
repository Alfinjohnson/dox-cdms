package org.gcdms.gcdmssaas.controller;


import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.model.CustomApiResponse;
import org.gcdms.gcdmssaas.payload.request.CreateSubscriberRequest;
import org.gcdms.gcdmssaas.payload.response.FetchAllSubscriberResponse;
import org.gcdms.gcdmssaas.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.gcdms.gcdmssaas.utility.AppConst.getCurrentTime;

/**
 * Subscriber resetController
 */
@RestController
@RequestMapping("/api/v1/subscriber")
@Slf4j(topic = "Subscriber RestController")
public class SubscriberController {

    /**
     * subscriber Service autowired
     */
    @Autowired
    private final SubscriberService subscriberService;

    public SubscriberController( SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }


    /**
     * @apiNote api for fetching all subscriber information
     * @return List of  subscriber information
     */
    @NonNull
    @GetMapping
    private Mono<ResponseEntity<CustomApiResponse<FetchAllSubscriberResponse>>> fetchAllSubscriber() {
        log.info("controller: fetchAllSubscriber ");
        return subscriberService.fetchAllSubscriber()
                .collectList() // Collect all emitted items into a List
                .map(subscribers -> {
                    CustomApiResponse<FetchAllSubscriberResponse> response = new CustomApiResponse<>();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Success");
                    response.setData(subscribers);
                    response.setTimestamp(getCurrentTime());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * @return Subscriber Id,
     * @apiNote api for creating a new subscriber
     */
    @NonNull
    @PostMapping
    private Mono<ResponseEntity<CustomApiResponse<Long>>> createSubscriber(@NonNull @RequestBody CreateSubscriberRequest createSubscriberRequest) {
        log.info("controller: createSubscriber");
        return subscriberService.createSubscription(createSubscriberRequest)
                .map(savedId -> {
                    CustomApiResponse<Long> response = new CustomApiResponse<>();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Success");
                    response.setData(Collections.singletonList(savedId));
                    response.setTimestamp(getCurrentTime());
                    return ResponseEntity.ok(response);
                });
    }

}
