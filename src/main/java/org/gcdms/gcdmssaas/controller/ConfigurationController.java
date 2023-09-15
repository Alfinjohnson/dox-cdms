package org.gcdms.gcdmssaas.controller;


import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.model.CustomApiResponse;
import org.gcdms.gcdmssaas.payload.request.CreateConfigurationRequest;
import org.gcdms.gcdmssaas.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.gcdms.gcdmssaas.utility.AppConst.getCurrentTime;

/**
 * Configuration Controller class
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1/configuration")
@Slf4j(topic = "RestController")
public class ConfigurationController {

    @Autowired
    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * @apiNote api design for testing application
     * @return Up on Success return, "application running successfully", String
     * */
    @NonNull
    @GetMapping(path = "/test")
    private Mono<ResponseEntity<CustomApiResponse<String>>> testApplication() {
        log.info("controller: test api");
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(Collections.singletonList("application running successfully"));
        response.setTimestamp(getCurrentTime());
        return Mono.just(ResponseEntity.ok(response)) ;
    }

    /**
     * @return configuration Id,
     * @apiNote endpoint for creating new Configuration
     */
    @NonNull
    @PostMapping
    private Mono<ResponseEntity<CustomApiResponse<Long>>> createConfiguration(@NonNull @RequestBody CreateConfigurationRequest createConfigurationRequest) {
        log.info("controller: createConfiguration");
        return configurationService.createConfiguration(createConfigurationRequest)
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
