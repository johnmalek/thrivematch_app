package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminInvestorInfoResponse {
    private String message;
    private boolean success;
    List<AdminInvestorDetails> investors;
}
