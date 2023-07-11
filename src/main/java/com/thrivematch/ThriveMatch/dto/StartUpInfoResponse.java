package com.thrivematch.ThriveMatch.dto;

import java.util.ArrayList;

public class StartUpInfoResponse {
    private boolean success;
    private String message;
    private ArrayList<StartUpDetails> startups;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<StartUpDetails> getStartups() {
        return startups;
    }

    public void setStartups(ArrayList<StartUpDetails> startups) {
        this.startups = startups;
    }
}
