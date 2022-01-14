package com.example.oindrila.employeemanagementsystem.models.requests;

import com.example.oindrila.employeemanagementsystem.enums.Department;
import com.example.oindrila.employeemanagementsystem.enums.Gender;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EmployeeUpdateRequest implements Serializable {

    private String firstName;

    private String lastName;

    private BigDecimal salary;

    private String position;

    private Department department;

    private Gender gender;

    private String email;
}
