package org.gcdms.gcdmssaas.controller;


import lombok.extern.slf4j.Slf4j;
import org.gcdms.gcdmssaas.payload.response.CustomApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static org.gcdms.gcdmssaas.utility.AppConst.getCurrentTime;

/**
 * resetController
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
@Slf4j(topic = "RestController")
public class RestController {

    /**
     * @apiNote get all employee method
     * @return GetEmployeeResponse
     * */
    @NonNull
    @GetMapping
    private ResponseEntity<CustomApiResponse<String>> testApi() {
        log.info("inside get all employees controller");
        CustomApiResponse<String> response = new CustomApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData("application running successfully");
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }
}
