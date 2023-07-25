package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminInvestorDetails {
    private Integer id;
    private String name;
    private String email;
    private String address;
    private LocalDate dateCreated;
    private String industry;
}
