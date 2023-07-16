package com.thrivematch.ThriveMatch.dto;

import java.util.ArrayList;

public class AllUserResponse {
    private boolean success;
    private String message;
    private ArrayList<UserDetailsDTO> users;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<UserDetailsDTO> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserDetailsDTO> userDetailDTOS) {
        this.users = userDetailDTOS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
