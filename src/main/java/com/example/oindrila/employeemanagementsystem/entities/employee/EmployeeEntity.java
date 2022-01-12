package com.example.oindrila.employeemanagementsystem.entities.employee;

import com.example.oindrila.employeemanagementsystem.enums.Department;
import com.example.oindrila.employeemanagementsystem.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "employee")
@Table(name = "employee")
@ToString(callSuper = true)
public class EmployeeEntity extends AbstractEntity {

    @Column(name = "first_name", columnDefinition = "VARCHAR(100)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(100)")
    private String lastName;

    @Column(name = "email", columnDefinition = "VARCHAR(50)")
    private String email;

    @Column
    private BigDecimal salary;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Department department;

    @Column(columnDefinition = "ENUM('M','F','O')")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "VARCHAR(50)")
    private String position;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)", unique = true)
    private String phone;



}
