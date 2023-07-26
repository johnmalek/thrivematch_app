package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AllInvestorDetails {
    private Integer id;
    private String name;
    private String description;
    private String industry;
    private String email;
    private String address;
    private String poBox;
    private LocalDate yearFounded;
    private String picturePath;
    private LocalDate dateCreated;
    private Integer user_id;
    private Integer admin_id;
}
