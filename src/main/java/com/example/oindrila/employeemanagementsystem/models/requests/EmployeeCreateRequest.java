package com.example.oindrila.employeemanagementsystem.models.requests;

//Request for registering new employee

import com.example.oindrila.employeemanagementsystem.enums.Department;
import com.example.oindrila.employeemanagementsystem.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreateRequest {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 100)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 100)
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    @Min(value = 1)
    private BigDecimal salary;

    @NotNull
    private Department department;

    @NotNull
    private Gender gender;

    @NotNull
    @NotEmpty
    private String position;


    @NotNull
    @NotEmpty
    @Size(min = 10, max = 20)
    private String phone;



}
