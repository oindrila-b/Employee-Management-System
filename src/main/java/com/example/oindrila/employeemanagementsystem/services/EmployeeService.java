package com.example.oindrila.employeemanagementsystem.services;

import com.example.oindrila.employeemanagementsystem.entities.employee.EmployeeEntity;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeCreateRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.FilterRequest;

import java.util.List;

public interface EmployeeService {

    EmployeeEntity addNewEmployee(final EmployeeCreateRequest employeeCreateRequest);

    List<EmployeeEntity> findEmployeesByFilterCriteria(final FilterRequest filterRequest);

}
