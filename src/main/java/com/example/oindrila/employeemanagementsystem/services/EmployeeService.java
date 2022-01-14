package com.example.oindrila.employeemanagementsystem.services;

import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeRegistrationRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeUpdateRequest;
import com.example.oindrila.employeemanagementsystem.models.requests.FilterRequest;
import com.example.oindrila.employeemanagementsystem.models.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee createEmployee(final EmployeeRegistrationRequest employeeRegistrationRequest);

    List<Employee> findEmployeesByFilterCriteria(final FilterRequest filterRequest);

    List<Employee> findAllEmployees();

    Optional<Employee> findById(final Long id);

    Optional<Employee> updateEmployeeById(final Long id, final EmployeeUpdateRequest employeeUpdateRequest);

    Boolean deleteById(final Long id);
}
