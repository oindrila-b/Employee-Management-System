package com.example.oindrila.employeemanagementsystem.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PhotoInfo extends AbstractModel {
    private String name;
    private String url;
}
