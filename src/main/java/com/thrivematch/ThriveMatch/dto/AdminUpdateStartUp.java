package com.thrivematch.ThriveMatch.dto;

import lombok.Data;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdminUpdateStartUp {
    private String email;
    private String name;
    private String description;
    private String industry;
    private String address;
    private String poBox;
    private String year;
    private MultipartFile  file;
}
