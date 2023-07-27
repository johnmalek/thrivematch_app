package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminStartUpDetails {
    private Integer id;
    private String name;
    private String email;
    private String location;
    private LocalDate dateCreated;
    private String industry;
}
