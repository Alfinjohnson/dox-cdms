package com.dox.cdms.controller;


import com.dox.cdms.model.CustomApiResponse;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.request.DeleteConfigurationRequest;
import com.dox.cdms.payload.request.UpdateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.payload.response.GetFullConfigurationResponse;
import com.dox.cdms.service.ConfigurationService;
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
@RequestMapping("/api/v1/configuration")
@Slf4j(topic = "RestConfigurationController")
public class ConfigurationController {

    @Autowired
    private final ConfigurationService configurationService;
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
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

    /**
     * @return configuration Entity,
     * @apiNote endpoint for creating new Configuration
     */
    @NonNull
    @PostMapping
    private ResponseEntity<CustomApiResponse<CreateConfigurationResponse>> createConfiguration(@NonNull @RequestBody CreateConfigurationRequest createConfigurationRequest) {
        logger.info("controller: createConfiguration");
        createConfigurationValidationMethod(createConfigurationRequest);
        CreateConfigurationResponse createConfigurationResponse = configurationService.createConfiguration(createConfigurationRequest);
        CustomApiResponse<CreateConfigurationResponse> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Success");
        response.setData(createConfigurationResponse);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }

    /**
     * @return configuration Entity,
     * @apiNote endpoint for updating Configuration
     */
    @NonNull
    @PutMapping
    private ResponseEntity<CustomApiResponse<String>> updateConfiguration(@NonNull @RequestBody UpdateConfigurationRequest updateConfigurationRequest) {
        log.info("controller: updateConfiguration");
        updateConfigurationValidationMethod(updateConfigurationRequest);
        int createConfigurationResponse = configurationService.updateConfiguration(updateConfigurationRequest);
        if (createConfigurationResponse == 0)  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update configuration: " + updateConfigurationRequest.getName());
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        response.setMessage("Success");
        response.setData("Updated");
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }

    /**
     * TODO: delete corresponding data from cdms and subscriber entities
     * @param deleteConfigurationRequest  deleteConfigurationRequest
     * @return String message
     */
    @NonNull
    @DeleteMapping
    private ResponseEntity<CustomApiResponse<String>> deleteConfiguration(@NonNull @RequestBody DeleteConfigurationRequest deleteConfigurationRequest) {
        log.info("controller: deleteConfiguration");
        deleteConfigurationValidationMethod(deleteConfigurationRequest);
        long createConfigurationResponse = configurationService.deleteConfiguration(deleteConfigurationRequest);
        if (createConfigurationResponse == 0)  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete configuration: " + deleteConfigurationRequest.getName());
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        response.setMessage("Success");
        response.setData(deleteConfigurationRequest.getName() +" deleted");
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }

    /**
     * @return configuration Entity,
     * @apiNote endpoint for creating new Configuration
     */
    @GetMapping("/f/{name}")
    private @NotNull ResponseEntity<CustomApiResponse<GetFullConfigurationResponse>> getFullConfiguration(@NonNull @RequestParam("name") String configName) {
        logger.info("controller: getFullConfiguration");
        getConfigurationValidationMethod(configName);
        GetFullConfigurationResponse getFullConfiguration = configurationService.getFullConfiguration(configName);
        CustomApiResponse<GetFullConfigurationResponse> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(getFullConfiguration);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }
}
