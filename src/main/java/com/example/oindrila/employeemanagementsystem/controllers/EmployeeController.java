package com.example.oindrila.employeemanagementsystem.controllers;

import com.example.oindrila.employeemanagementsystem.entities.employee.EmployeeEntity;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeCreateRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.FilterRequest;
import com.example.oindrila.employeemanagementsystem.models.responses.EmployeeCreatedResponse;
import com.example.oindrila.employeemanagementsystem.services.EmployeeService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void configureObjectMapper() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @PostMapping
    public ResponseEntity<EmployeeCreatedResponse> addNewEmployee(@Validated @RequestBody final EmployeeCreateRequest employeeCreateRequest) {
        log.info("New User Creation Request received {}", employeeCreateRequest);
        final EmployeeEntity employeeEntity = employeeService.addNewEmployee(employeeCreateRequest);
        log.info("User Created Response Body Received {}", employeeEntity);
        log.info("Converting employee entity to employee created response");
        final EmployeeCreatedResponse employeeCreatedResponse = objectMapper.convertValue(employeeEntity, EmployeeCreatedResponse.class);
        log.info("Converted employee entity [at controller level] {} : ", employeeCreatedResponse);
        return ResponseEntity.ok(employeeCreatedResponse);
    }

    @PostMapping(value = "filter-request")
    public ResponseEntity<List<EmployeeCreatedResponse>> getEmployeeListByCriteria(@Validated @RequestBody final FilterRequest filterRequest){
        log.info("Get Employee list by filters request received : {} ",filterRequest);
        final List<EmployeeEntity> employeesByCriteria = employeeService.findEmployeesByFilterCriteria(filterRequest);
        final List<EmployeeCreatedResponse> employeeCreatedResponseList = new ArrayList<>();
        employeesByCriteria.forEach(employeeEntity -> {
            final EmployeeCreatedResponse employeeCreatedResponse = objectMapper.convertValue(employeeEntity, EmployeeCreatedResponse.class);
            employeeCreatedResponseList.add(employeeCreatedResponse);
        });
        log.info("Retrieved List of Employees based on criteria {}: {}", filterRequest, employeeCreatedResponseList);
        return ResponseEntity.ok(employeeCreatedResponseList);
    }

    @GetMapping(params = {"page-limit"})
    public ResponseEntity<List<EmployeeCreatedResponse>> getEmployeeListByPage(@RequestParam("page-limit") @Min(1) final Integer pageLimit) {
        log.info("Get Employee list by Page Limit {} \n\n ", pageLimit);
        final FilterRequest filterRequest = new FilterRequest();
        filterRequest.setPageLimit(pageLimit);
         return getEmployeeListByCriteria(filterRequest);
    }


}
