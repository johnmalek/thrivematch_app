package com.thrivematch.ThriveMatch.dto;

import java.util.ArrayList;

public class InvestorInfoResponse {
    private boolean success;
    private String message;
    private ArrayList<InvestorDetails> investors;

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

    public ArrayList<InvestorDetails> getInvestors() {
        return investors;
    }

    public void setInvestors(ArrayList<InvestorDetails> investors) {
        this.investors = investors;
    }
}
