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

   /*

    @Autowired
    private EmployeeService employeeService;

    public RestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    *//**
     * @apiNote get all employee method
     * @return GetEmployeeResponse
     * *//*
    @NonNull
    @GetMapping
    private ResponseEntity<ApiResponse<List<GetEmployeeResponse>>> getAllEmployees() {
        log.info("inside get all employees controller");
        List<GetEmployeeResponse> getAllEmployee = employeeService.findAll();
        ApiResponse<List<GetEmployeeResponse>> response = new ApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(getAllEmployee);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response);
    }

    *//**
     * @apiNote get employee by id controller
     * @param id
     * @return GetEmployeeResponse
     *//*
    @NonNull
    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse<GetEmployeeResponse>> getEmployeeById(@PathVariable String id) {
        log.info("inside get all get employees by id controller");
        if (isStringNullOrEmptyOrBlank(id)) throw new CustomException(HttpStatus.BAD_REQUEST, "EmployeeId not present");
        if (!isValidAlphaNumeric(id)) throw new CustomException(HttpStatus.BAD_REQUEST, "EmployeeId not valid");

        GetEmployeeResponse getAllEmployee = employeeService.findById(id);
        ApiResponse<GetEmployeeResponse> response = new ApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(getAllEmployee);
        response.setTimestamp(getCurrentTime());
        return ResponseEntity.ok(response) ;
    }

    *//**
     * @apiNote create employee controller
     * *//*
    @NonNull
    @PostMapping
    private ResponseEntity<ApiResponse<CreateEmployeeResponse>> createEmployee(@NonNull @RequestBody CreateEmployeeRequest createEmployeeRequest)   {
        log.info("inside create employee controller");
        CreateEmployeeRequest newCreateEmployeeRequest = createEmployeeRequestValidationMethod(createEmployeeRequest);
        CreateEmployeeResponse employeeResponse= employeeService.createEmployee(newCreateEmployeeRequest);
        ApiResponse<CreateEmployeeResponse> response = new ApiResponse<>();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("New Employee Created");
        response.setData(employeeResponse);
        response.setTimestamp(getCurrentTime());

        return ResponseEntity.ok(response);
    }


    *//**
     * @apiNote update employee method
     * @param id
     * @param updateEmployeeRequest
     * @return
     *//*
    @NonNull
    @PutMapping("/{id}")
    private ResponseEntity<ApiResponse<UpdateEmployeeResponse>> updateEmployee(@PathVariable String id, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        log.info("inside update employee controller");
        UpdateEmployeeRequest newUpdateEmployeeDTO = updateEmployeeRequestValidationMethod(id, updateEmployeeRequest);
        UpdateEmployeeResponse updateEmployeeResponse = employeeService.updateEmployee(id, newUpdateEmployeeDTO);
        ApiResponse<UpdateEmployeeResponse> response = new ApiResponse<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Employee updated");
        response.setData(updateEmployeeResponse);
        response.setTimestamp(getCurrentTime());

        return ResponseEntity.ok(response);
    }


    *//**
     * @apiNote delete employee controller
     * @param id
     * @return
     *//*
    @NonNull
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        log.info("inside delete employee controller");
        final boolean isEmployeeIdNullOrEmptyOrBlank = isStringNullOrEmptyOrBlank(id);
        if (isEmployeeIdNullOrEmptyOrBlank) throw new CustomException(HttpStatus.BAD_REQUEST, "Employee Id can't be null or empty");
        if (!isValidAlphaNumeric(id)) throw new CustomException(HttpStatus.BAD_REQUEST, "Employee Id format not valid");
        if (!employeeService.delete(id))throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed delete employee:n"+id);
        return ResponseEntity.noContent().build();
    }*/
}
