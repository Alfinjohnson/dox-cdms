package com.dox.cdms.controller;


import com.dox.cdms.entity.SubscriberEntity;
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
 * Subscriber Controller class
 */
@RestController
@RequestMapping("/api/v1/subscriber")
@Slf4j(topic = "RestSubscriberController")
public class SubscriberController {
    @Autowired
    private SubscriberService subscriberService;
    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }
    /**
     * API for testing the application.
     *
     * @return A ResponseEntity with a CustomApiResponse containing a success message.
     */
    @NonNull
    @GetMapping(path = "/test")
    private ResponseEntity<CustomApiResponse<String>> testApplication() {
        try {
            logger.info("Controller: testApplication");
            CustomApiResponse<String> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setData("Application running successfully");
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: testApplication - Error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to test application");
        }
    }

    /**
     * Updates a subscriber by referencing the ID.
     *
     * @param updateSubscriberRequest DTO containing update information.
     * @return A ResponseEntity with a CustomApiResponse containing the updated SubscriberEntity.
     */
    @NonNull
    @PutMapping
    private ResponseEntity<CustomApiResponse<SubscriberEntity>> updateSubscriber(
            @NonNull @RequestBody UpdateSubscriberRequest updateSubscriberRequest) {
        try {
            log.info("Controller: updateSubscriber");
            updateSubscriberValidationMethod(updateSubscriberRequest);
            SubscriberEntity updateSubscriberResponse = subscriberService.updateSubscriber(updateSubscriberRequest);
            log.debug("UpdateSubscriberResponse: {}", updateSubscriberResponse);
            CustomApiResponse<SubscriberEntity> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.ACCEPTED.value());
            response.setMessage("Success");
            response.setData(updateSubscriberResponse);
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Controller: updateSubscriber - Error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update subscriber");
        }
    }

    /**
     * Deletes a subscriber by ID.
     *
     * @param subscriberId The ID of the subscriber to be deleted.
     * @return A ResponseEntity with a CustomApiResponse containing a success message.
     */
    @NonNull
    @DeleteMapping("/{id}")
    private ResponseEntity<CustomApiResponse<String>> deleteSubscriber(
            @NonNull @RequestParam("id") Long subscriberId) {
        try {
            log.info("Controller: deleteSubscriber");
            if (subscriberId < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscriber ID is empty");
            subscriberService.deleteSubscriber(subscriberId);
            log.debug("Subscriber: {} deleted successfully", subscriberId);
            CustomApiResponse<String> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.ACCEPTED.value());
            response.setMessage("Success");
            response.setData(subscriberId + " deleted");
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Controller: deleteSubscriber - Error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete subscriber");
        }
    }
    /**
     * Retrieves a subscriber by ID.
     *
     * @param subscriberId The ID of the subscriber to retrieve.
     * @return A ResponseEntity containing a CustomApiResponse with subscriber data.
     */
    @RequestMapping("/{id}")
    private @NotNull ResponseEntity<CustomApiResponse<SubscribersDataModel>> getSubscriber(
            @NonNull @RequestParam("id") Long subscriberId) {
        try {
            logger.info("Controller: getSubscriber {}", subscriberId);
            // Validate subscriberId
            if (subscriberId < 1) {
                logger.error("Invalid subscriber id: {}", subscriberId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscriber id is empty or invalid");
            }
            // Retrieve subscriber data
            SubscribersDataModel getSubscriberResponse = subscriberService.getSubscriber(subscriberId);
            // Build response
            CustomApiResponse<SubscribersDataModel> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setData(getSubscriberResponse);
            response.setTimestamp(getCurrentTime());
            // Log success
            logger.info("Controller: getSubscriber - Success for subscriberId: {}", subscriberId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log error
            logger.error("Controller: getSubscriber - Error for subscriberId {}: {}", subscriberId, e.getMessage());
            // Return error response
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve subscriber data");
        }
    }
}
