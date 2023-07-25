package com.thrivematch.ThriveMatch.dto;

import lombok.Data;

import java.sql.Struct;
import java.util.List;

@Data
public class AdminStartUpInfoResponse {
    private String message;
    private boolean success;
    List<AdminStartUpDetails> startups;
}
