package com.example.oindrila.employeemanagementsystem.models.requests;

import com.example.oindrila.employeemanagementsystem.enums.Department;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class FilterRequest {

    private Department department;
    private String position;

    @Min(value = 1, message = "Minimum number of pages cannot be lower than 1")
    private Integer pageLimit = 10;
}
