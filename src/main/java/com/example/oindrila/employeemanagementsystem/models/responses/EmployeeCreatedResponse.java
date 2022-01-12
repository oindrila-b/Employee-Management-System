package com.example.oindrila.employeemanagementsystem.models.responses;

import com.example.oindrila.employeemanagementsystem.enums.Department;
import com.example.oindrila.employeemanagementsystem.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreatedResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private Department department;
    private Gender gender;
    private String position;
    private String email;
    private String phone;


}
