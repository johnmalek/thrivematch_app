package com.thrivematch.ThriveMatch.dto;

import java.util.ArrayList;

public class AllUserResponse {
    private boolean success;
    private String message;
    private ArrayList<UserDetails> users;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<UserDetails> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserDetails> userDetails) {
        this.users = userDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
