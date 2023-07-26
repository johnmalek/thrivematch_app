package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllStartUpsInformationResponse {
    private boolean success;
    private String message;
    List<AllStartUpDetails> startUpDetailsList;
}
