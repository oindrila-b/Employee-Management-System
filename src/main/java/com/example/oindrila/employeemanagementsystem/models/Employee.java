package com.example.oindrila.employeemanagementsystem.models;

import com.example.oindrila.employeemanagementsystem.enums.Department;
import com.example.oindrila.employeemanagementsystem.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends AbstractModel {

    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private Department department;
    private Gender gender;
    private String position;
    private String email;
    private String phone;
    private PhotoInfo photoInfo;
}
