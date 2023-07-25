package com.thrivematch.ThriveMatch.dto;

import com.thrivematch.ThriveMatch.model.UserEntity;

class userDetails{
    private boolean hasCreatedStartUp;
    private boolean hasCreatedInvestor;
    private boolean hasCreatedIndividualInvestor;
    private String username;
    private String email;
    private Integer id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public userDetails(boolean hasCreatedStartUp, boolean hasCreatedInvestor, boolean hasCreatedIndividualInvestor, String username, String email, Integer id) {
        this.hasCreatedStartUp = hasCreatedStartUp;
        this.hasCreatedInvestor = hasCreatedInvestor;
        this.hasCreatedIndividualInvestor = hasCreatedIndividualInvestor;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public boolean isHasCreatedStartUp() {
        return hasCreatedStartUp;
    }

    public void setHasCreatedStartUp(boolean hasCreatedStartUp) {
        this.hasCreatedStartUp = hasCreatedStartUp;
    }

    public boolean isHasCreatedInvestor() {
        return hasCreatedInvestor;
    }

    public void setHasCreatedInvestor(boolean hasCreatedInvestor) {
        this.hasCreatedInvestor = hasCreatedInvestor;
    }

    public boolean isHasCreatedIndividualInvestor() {
        return hasCreatedIndividualInvestor;
    }

    public void setHasCreatedIndividualInvestor(boolean hasCreatedIndividualInvestor) {
        this.hasCreatedIndividualInvestor = hasCreatedIndividualInvestor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

public class UserLoginResponse {
    private boolean success;
    private String message;
    private String token;
    private userDetails user;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public userDetails getUser() {
        return user;
    }

    public void setUser(userDetails user) {
        this.user = user;
    }

    public void setUser(Integer id, String username, String email, boolean hasCreatedStartUp, boolean hasCreatedInvestor, boolean hasCreatedIndividualInvestor) {
        this.user = new userDetails(hasCreatedStartUp, hasCreatedInvestor, hasCreatedIndividualInvestor, username, email, id);
    }
}
