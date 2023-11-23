package com.dox.cdms.controller;


import com.dox.cdms.model.CustomApiResponse;
import com.dox.cdms.payload.request.CreateConfigurationRequest;
import com.dox.cdms.payload.request.DeleteConfigurationRequest;
import com.dox.cdms.payload.request.GetConfigurationRequest;
import com.dox.cdms.payload.request.UpdateConfigurationRequest;
import com.dox.cdms.payload.response.CreateConfigurationResponse;
import com.dox.cdms.payload.response.GetConfigurationResponse;
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
    private ConfigurationService configurationService;
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * API for testing the application.
     *
     * @return A ResponseEntity with a CustomApiResponse containing a success message.
     */
    @NonNull
    @GetMapping(path = "/test")
    private ResponseEntity<CustomApiResponse<String>> testApplication() {
            logger.info("Controller: testApplication");
            CustomApiResponse<String> response = new CustomApiResponse<>();
            logger.info("Response: Application running successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setData("Application running successfully");
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
    }

    /**
     * Creates a new configuration.
     *
     * @param createConfigurationRequest DTO containing configuration creation information.
     * @return A ResponseEntity with a CustomApiResponse containing the created configuration.
     */
    @NonNull
    @PostMapping
    private ResponseEntity<CustomApiResponse<CreateConfigurationResponse>> createConfiguration(
            @NonNull @RequestBody CreateConfigurationRequest createConfigurationRequest) {
            logger.info("Controller: createConfiguration");
            createConfigurationValidationMethod(createConfigurationRequest);
            CreateConfigurationResponse createConfigurationResponse = configurationService.createConfiguration(createConfigurationRequest);
            logger.info("Create Configuration Response: {}", createConfigurationResponse);
            CustomApiResponse<CreateConfigurationResponse> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Success");
            response.setData(createConfigurationResponse);
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
    }

    /**
     * Updates a configuration.
     *
     * @param updateConfigurationRequest DTO containing configuration update information.
     * @return A ResponseEntity with a CustomApiResponse containing a success message.
     */
    @NonNull
    @PutMapping
    private ResponseEntity<CustomApiResponse<String>> updateConfiguration(
            @NonNull @RequestBody UpdateConfigurationRequest updateConfigurationRequest) {
            log.info("Controller: updateConfiguration {}", updateConfigurationRequest);
            updateConfigurationValidationMethod(updateConfigurationRequest);
            int updateConfigurationResponse = configurationService.updateConfiguration(updateConfigurationRequest);
            if (updateConfigurationResponse == 0)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update configuration: " + updateConfigurationRequest.getName());
            CustomApiResponse<String> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.ACCEPTED.value());
            response.setMessage("Success");
            response.setData("Updated");
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
    }

    /**
     * Deletes a configuration and its corresponding data from cdms and subscriber entities.
     *
     * @param deleteConfigurationRequest DTO containing configuration deletion information.
     * @return A ResponseEntity with a CustomApiResponse containing a success message.
     */
    @NonNull
    @DeleteMapping
    private ResponseEntity<CustomApiResponse<String>> deleteConfiguration(
            @NonNull @RequestBody DeleteConfigurationRequest deleteConfigurationRequest) {
            log.info("Controller: deleteConfiguration {}", deleteConfigurationRequest);
            deleteConfigurationValidationMethod(deleteConfigurationRequest);
            long deleteConfigurationResponse = configurationService.deleteConfiguration(deleteConfigurationRequest);
            if (deleteConfigurationResponse == 0)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete configuration: " + deleteConfigurationRequest.getName());
            logger.info("DeleteConfiguration done");
            CustomApiResponse<String> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.ACCEPTED.value());
            response.setMessage("Success");
            response.setData(deleteConfigurationRequest.getName() + " deleted");
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
    }

    /**
     * Gets the full configuration by name.
     *
     * @param configName The name of the configuration to retrieve.
     * @return A ResponseEntity with a CustomApiResponse containing the full configuration.
     */
    @GetMapping("/f/{name}")
    private @NotNull ResponseEntity<CustomApiResponse<GetFullConfigurationResponse>> getFullConfiguration(
            @NonNull @RequestParam("name") String configName) {
            logger.info("Controller: getFullConfiguration");
            getFullConfigurationValidationMethod(configName);
            GetFullConfigurationResponse getFullConfiguration = configurationService.getFullConfiguration(configName);
            logger.info("Get Full Configuration Response: {}", getFullConfiguration);
            CustomApiResponse<GetFullConfigurationResponse> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setData(getFullConfiguration);
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
    }

    /**
     * Serves configuration on request.
     *
     * @param getConfigurationRequest DTO containing configuration retrieval information.
     * @return A ResponseEntity with a CustomApiResponse containing the requested configuration.
     */
    @GetMapping
    private @NotNull ResponseEntity<CustomApiResponse<GetConfigurationResponse>> getConfiguration(
            @NonNull @RequestBody GetConfigurationRequest getConfigurationRequest) {
            logger.info("Controller: getConfiguration");
            getConfigurationValidationMethod(getConfigurationRequest);
            GetConfigurationResponse getConfigurationResponse = configurationService.getConfiguration(getConfigurationRequest);
            logger.info("Get Configuration Response : {}", getConfigurationResponse);
            CustomApiResponse<GetConfigurationResponse> response = new CustomApiResponse<>();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Success");
            response.setData(getConfigurationResponse);
            response.setTimestamp(getCurrentTime());
            return ResponseEntity.ok(response);
    }
}
