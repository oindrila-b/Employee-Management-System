package com.example.oindrila.employeemanagementsystem.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AbstractModel {
    private Long id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
