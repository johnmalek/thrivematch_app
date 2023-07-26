package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllInvestorsInformationResponse {
    private boolean success;
    private String message;
    List<AllInvestorDetails> investorDetailsList;
}
