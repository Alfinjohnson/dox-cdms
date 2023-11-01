package org.gcdms.gcdmssaas.controller;


import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.model.CustomApiResponse;
import org.gcdms.gcdmssaas.payload.request.CreateDataTypeRequest;
import org.gcdms.gcdmssaas.payload.response.CreateDataTypeResponse;
import org.gcdms.gcdmssaas.payload.response.FetchAllDataTypeResponse;
import org.gcdms.gcdmssaas.service.CSDMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.gcdms.gcdmssaas.utility.AppConst.getCurrentTime;

/**
 * Data type resetController
 */
@RestController
@RequestMapping("/api/v1/data-type")
@Slf4j(topic = "DataType RestController")
public class DataTypeController {

    /**
     * datatype Service autowired
     */
    @Autowired
    private final CSDMappingService dataTypeService;

    public DataTypeController(CSDMappingService dataTypeService) {
        this.dataTypeService = dataTypeService;
    }

    /**
     * @apiNote api for fetching all the datatype information
     * @return List of  datatype information
     */
    @NonNull
    @GetMapping
    private Mono<ResponseEntity<CustomApiResponse<FetchAllDataTypeResponse>>> fetchAllDatatype() {
        log.info("controller: fetchAllDatatype ");
        return dataTypeService.fetchAllDatatype()
                .collectList() // Collect all emitted items into a List
                .map(datatype -> {
                    CustomApiResponse<FetchAllDataTypeResponse> response = new CustomApiResponse<>();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Success");
                    response.setData(datatype);
                    response.setTimestamp(getCurrentTime());
                    return ResponseEntity.ok(response);
                });
    }

    /**
     * @return DataType Entity Response,
     * @apiNote api for creating a new subscriber
     */
    @NonNull
    @PostMapping
    private Mono<ResponseEntity<CustomApiResponse<CreateDataTypeResponse>>> createDataType(@NonNull @RequestBody CreateDataTypeRequest createDataTypeRequest) {
        log.info("controller: createDataType");
        return dataTypeService.createCSDMapping(createDataTypeRequest)
                .map(savedId -> {
                    CustomApiResponse<CreateDataTypeResponse> response = new CustomApiResponse<>();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Success");
                    response.setData(Collections.singletonList(savedId));
                    response.setTimestamp(getCurrentTime());
                    return ResponseEntity.ok(response);
                });
    }
}
