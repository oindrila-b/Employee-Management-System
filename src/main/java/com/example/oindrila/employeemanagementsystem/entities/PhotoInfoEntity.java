package com.example.oindrila.employeemanagementsystem.entities;

import com.example.oindrila.employeemanagementsystem.entities.employee.EmployeeEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "photoInfo")
@Table(name = "photo_info")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(value = {"employeeEntity"})
public class PhotoInfoEntity extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", unique = true)
    private EmployeeEntity employeeEntity;

    @Column(name = "name")
    private String name;
}
