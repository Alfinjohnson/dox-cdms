package com.dox.cdms.controller;


import com.dox.cdms.model.CustomApiResponse;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static com.dox.cdms.utility.AppConst.getCurrentTime;
import static com.dox.cdms.utility.CustomValidations.createConfigurationValidationMethod;

/**
 * Configuration Controller class
 */
@RestController
@RequestMapping("/api/v1/configuration")
@Slf4j(topic = "RestController")
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
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(createConfigurationResponse);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }

    /**
     * @return configuration Entity,
     * @apiNote endpoint for updating Configuration

    @NonNull
    @PutMapping
    private ResponseEntity<CustomApiResponse<CreateConfigurationResponse>> updateConfiguration(@NonNull @RequestBody UpdateConfigurationRequest updateConfigurationRequest) {
        log.info("controller: updateConfiguration");
        updateConfigurationValidationMethod(updateConfigurationRequest);
        CreateConfigurationResponse createConfigurationResponse = configurationService.updateConfiguration(updateConfigurationRequest);
        CustomApiResponse<CreateConfigurationResponse> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(createConfigurationResponse);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }
     */
}
