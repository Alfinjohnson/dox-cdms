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

import static org.gcdms.gcdmssaas.utility.AppConst.getCurrentTime;

/**
 * resetController
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
@Slf4j(topic = "RestController")
public class V1Controller {

    @Autowired
    private final ConfigurationService configurationService;

    public V1Controller(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * @apiNote test api
     * @return Up on Success return, application running successfully
     * */
    @NonNull
    @GetMapping
    private Mono<ResponseEntity<CustomApiResponse<String>>> testApi() {
        log.info("inside test api controller");
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData("application running successfully");
        response.setTimestamp(getCurrentTime());
        return Mono.just(ResponseEntity.ok(response)) ;
    }

    /**
     * @return Up on Success return, application running successfully
     * @apiNote testDB api
     */
    @NonNull
    @PostMapping
    private Mono<ResponseEntity<CustomApiResponse<Long>>> testDB(@NonNull @RequestBody CreateConfigurationRequest createConfigurationRequest) {
        return configurationService.createConfiguration(createConfigurationRequest)
                .map(savedId -> {
                    CustomApiResponse<Long> response = new CustomApiResponse<>();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Success");
                    response.setData(savedId);
                    response.setTimestamp(getCurrentTime());
                    return ResponseEntity.ok(response);
                });
    }

}
