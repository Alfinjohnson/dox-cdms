package com.dox.cdms.controller;


import com.dox.cdms.model.CustomApiResponse;
import com.dox.cdms.model.SubscribersDataModel;
import com.dox.cdms.payload.request.UpdateSubscriberRequest;
import com.dox.cdms.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.dox.cdms.utility.AppConst.getCurrentTime;
import static com.dox.cdms.utility.CustomValidations.*;

/**
 * Configuration Controller class
 */
@RestController
@RequestMapping("/api/v1/subscriber")
@Slf4j(topic = "RestSubscriberController")
public class SubscriberController {
    @Autowired
    private final SubscriberService subscriberService;
    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }
    /**
     * @apiNote api design for testing application
     * @return Up on Success return, "application running successfully", String
     * */
    @NonNull
    @GetMapping(path = "/test")
    private ResponseEntity<CustomApiResponse<String>> testApplication() {
        logger.info("controller: test api");
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData("application running successfully");
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response) ;
    }
    @NonNull
    @PutMapping
    private ResponseEntity<CustomApiResponse<SubscribersDataModel>> updateSubscriber(@NonNull @RequestBody UpdateSubscriberRequest updateSubscriberRequest) {
        log.info("controller: updateSubscriber");
        updateSubscriberValidationMethod(updateSubscriberRequest);
        int updateSubscriberResponse = subscriberService.updateSubscriber(updateSubscriberRequest);
        if (updateSubscriberResponse == 0)  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to update subscriber");
        SubscribersDataModel getSubscriberResponse = subscriberService.getSubscriber(updateSubscriberRequest.getId());
        CustomApiResponse<SubscribersDataModel> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        response.setMessage("Success");
        response.setData(getSubscriberResponse);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }
    @NonNull
    @DeleteMapping("/{id}")
    private ResponseEntity<CustomApiResponse<String>> deleteSubscriber(@NonNull @RequestParam("id") Long subscriberId) {
        log.info("controller: deleteSubscriber");
        if (subscriberId < 1)  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "subscriber id is empty");
        subscriberService.deleteSubscriber(subscriberId);
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        response.setMessage("Success");
        response.setData(subscriberId +" deleted");
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }
    /**
     * @return configuration Entity,
     * @apiNote endpoint for creating new Configuration
     */
    @GetMapping("/{id}")
    private @NotNull ResponseEntity<CustomApiResponse<SubscribersDataModel>> getSubscriber(@NonNull @RequestParam("id") Long subscriberId) {
        logger.info("controller: getSubscriber {}",subscriberId);
        if (subscriberId < 1)  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "subscriber id is empty");
        SubscribersDataModel getSubscriberResponse = subscriberService.getSubscriber(subscriberId);
        CustomApiResponse<SubscribersDataModel> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(getSubscriberResponse);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }
}
