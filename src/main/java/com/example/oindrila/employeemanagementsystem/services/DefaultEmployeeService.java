package com.example.oindrila.employeemanagementsystem.services;

import com.example.oindrila.employeemanagementsystem.entities.employee.EmployeeEntity;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeCreateRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.FilterRequest;
import com.example.oindrila.employeemanagementsystem.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class DefaultEmployeeService implements EmployeeService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    private void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public EmployeeEntity addNewEmployee(final EmployeeCreateRequest employeeCreateRequest) {
        log.info("Received Employee Create Request: {}", employeeCreateRequest);
        EmployeeEntity employeeEntity = objectMapper.convertValue(employeeCreateRequest, EmployeeEntity.class);
        employeeEntity = employeeRepository.save(employeeEntity);
        log.info("Received Created response body: {}", employeeEntity);
        return  employeeEntity;

    }

    @Override
    public List<EmployeeEntity> findEmployeesByFilterCriteria(final FilterRequest filterRequest) {
        log.info("Filter Request received  : {} ", filterRequest);
        final EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setCreatedOn(null);
        if (filterRequest.getDepartment() != null) {
            employeeEntity.setDepartment(filterRequest.getDepartment());
        }
        if (StringUtils.hasText(filterRequest.getPosition())) {
            employeeEntity.setPosition(filterRequest.getPosition());
        }
        log.info("Example Entity: {}", employeeEntity);
        final Example<EmployeeEntity> example = Example.of(employeeEntity);
        final Page<EmployeeEntity> employeeEntityPage = employeeRepository.findAll(example, PageRequest.of(0, filterRequest.getPageLimit()));
        log.info("Retrieved Employee Entities: {}", employeeEntityPage);
        return employeeEntityPage.getContent();
    }

}
