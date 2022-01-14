package com.example.oindrila.employeemanagementsystem.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ImageResource {
    private String fileName;
    private MultipartFile file;

    public ImageResource(final MultipartFile file, final String fileName) {
        this.fileName = fileName;
        this.file = file;
    }
}
