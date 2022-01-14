package com.example.oindrila.employeemanagementsystem.utilities;

import com.example.oindrila.employeemanagementsystem.models.requests.EmployeeRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
public class ResourceUtility {

    public static String generateFileName(final EmployeeRegistrationRequest employeeRegistrationRequest) {
        log.info("Generating File Name for Employee Registration Request: {}", employeeRegistrationRequest);
        final String[] firstNameArray = employeeRegistrationRequest.getFirstName().split(" ");
        final String fileName = firstNameArray[0] + employeeRegistrationRequest.getPhone() + getExtension(employeeRegistrationRequest.getImage());
        log.info("Generated File Name: {}", fileName);
        return fileName;
    }

    public static String getExtension(final MultipartFile multipartFile) {
        Objects.requireNonNull(multipartFile);
        if (!StringUtils.hasText(multipartFile.getOriginalFilename())) {
            throw new RuntimeException("Invalid Original File Name");
        }
        return multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
    }
}
