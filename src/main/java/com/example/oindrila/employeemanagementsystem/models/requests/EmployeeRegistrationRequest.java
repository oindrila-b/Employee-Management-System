package com.example.oindrila.employeemanagementsystem.models.requests;

import com.example.oindrila.employeemanagementsystem.enums.Department;
import com.example.oindrila.employeemanagementsystem.enums.Gender;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class EmployeeRegistrationRequest {

    @NotNull(message = "firstName cannot be null")
    @NotEmpty(message = "firstName cannot be empty")
    @Size(min = 3, max = 100, message = "firstName should be between length of 3 to 100")
    private String firstName;

    @NotNull(message = "lastName cannot be null")
    @NotEmpty(message = "lastName cannot be null")
    @Size(min = 1, max = 100, message = "lastName should be between length of 3 to 100")
    private String lastName;

    @NotNull(message = "email cannot be null")
    @NotEmpty(message = "email cannot be null")
    @Size(min = 5, max = 50, message = "email should be between length of 3 to 100")
    private String email;

    @NotNull(message = "salary cannot be null")
    @Min(value = 1, message = "salary cannot be lower than 1")
    private BigDecimal salary;

    @NotNull(message = "department cannot be null")
    private Department department;

    @NotNull(message = "gender cannot be null")
    private Gender gender;

    @NotNull(message = "position cannot be null")
    @NotEmpty(message = "position cannot be empty")
    private String position;

    @NotNull(message = "phone cannot be null")
    @NotEmpty(message = "phone cannot be empty")
    @Size(min = 10, max = 20, message = "phone should be between the length of 10 and 20")
    private String phone;

    @NotNull(message = "image cannot be null")
    private MultipartFile image;
}
