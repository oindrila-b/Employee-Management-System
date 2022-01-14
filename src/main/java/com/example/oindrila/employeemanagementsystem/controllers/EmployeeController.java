package com.example.oindrila.employeemanagementsystem.controllers;

import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeRegistrationRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeUpdateRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.FilterRequest;
import com.example.oindrila.employeemanagementsystem.models.Employee;
import com.example.oindrila.employeemanagementsystem.services.EmployeeService;
import com.example.oindrila.employeemanagementsystem.services.ImageService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@Slf4j
@RequestMapping(value = "employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ImageService profilePhotoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void configureObjectMapper() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @PostMapping(value = "filter-request")
    public @ResponseBody ResponseEntity<List<Employee>> getEmployeeListByCriteria(@Validated @RequestBody final FilterRequest filterRequest){
        log.info("Get Employee list by filters request received : {} ",filterRequest);
        final List<Employee> employeesByCriteria = employeeService.findEmployeesByFilterCriteria(filterRequest);
        log.info("Retrieved List of Employees based on criteria {}: {}", filterRequest, employeesByCriteria);
        return ResponseEntity.ok(employeesByCriteria);
    }

    @GetMapping(params = {"page-limit"})
    public @ResponseBody ResponseEntity<List<Employee>> getEmployeeListByPage(@RequestParam("page-limit") @Min(1) final Integer pageLimit) {
        log.info("Get Employee list by Page Limit {} \n\n ", pageLimit);
        final FilterRequest filterRequest = new FilterRequest();
        filterRequest.setPageLimit(pageLimit);
         return getEmployeeListByCriteria(filterRequest);
    }

    @GetMapping("registration")
    public String getRegistrationForm(final Model model) {
        log.info("Getting registration form");
        model.addAttribute("registration", new EmployeeRegistrationRequest());
        return "registration";
    }

    @PostMapping(value = "registration/done", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerEmployee(@Validated @ModelAttribute EmployeeRegistrationRequest request) throws Exception {
        log.info("Received Employee Registration Request: {}", request);
        final Employee employee = employeeService.createEmployee(request);
        log.info("Created Employee: {}", employee);
        return ResponseEntity.ok("Success");
    }

    @GetMapping
    public String getAllEmployees(final Model model) {
        log.info("Getting all employees");
        model.addAttribute("employees", employeeService.findAllEmployees());
        return "employee_list";
    }

    @GetMapping("{id}")
    public String getEmployeeById(@PathVariable("id") final Long id, final Model model) {
        log.info("Fetching Employee info by ID: {}", id);
        final Employee employee = employeeService.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "employee_update";
    }

    @PatchMapping(value = "{id}", consumes = {"application/json"})
    public @ResponseBody ResponseEntity<String> getEmployeeById(@PathVariable("id") final Long id, @RequestBody final EmployeeUpdateRequest employeeUpdateRequest) {
        log.info("Received Employee Update Request: {} for ID: {}", employeeUpdateRequest, id);
        final AtomicReference<String> message = new AtomicReference<>("Failure");
        final AtomicReference<HttpStatus> httpStatusAtomicReference = new AtomicReference<>(HttpStatus.NOT_FOUND);
        employeeService.updateEmployeeById(id, employeeUpdateRequest).ifPresentOrElse((e) -> {
                    log.info("Updated Employee");
                    message.set("Success");
                    httpStatusAtomicReference.set(HttpStatus.ACCEPTED);
                }, () -> {
            log.error("Could not find employee details by ID: " + id);
        });
        return new ResponseEntity<>(message.get(), httpStatusAtomicReference.get());
    }

    @DeleteMapping("{id}")
    public @ResponseBody ResponseEntity<String> deleteEmployeeById(@PathVariable("id") final Long id) {
        log.info("Received Delete request for ID: {}", id);
        employeeService.deleteById(id);
        return ResponseEntity.ok("Success");
    }


}
