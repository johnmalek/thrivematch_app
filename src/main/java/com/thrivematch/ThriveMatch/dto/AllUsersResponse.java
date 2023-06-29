package com.thrivematch.ThriveMatch.dto;

class UserDetails{
    private String username;
    private String email;
    private Integer id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDetails(String username, String email, Integer id) {
        this.username = username;
        this.email = email;
        this.id = id;
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

public class AllUsersResponse {
    private boolean success;
    private String message;
    private userDetails user;

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

    public userDetails getUser() {
        return user;
    }

    public void setUser(String username, String email, Integer id) {
        this.user = new userDetails(username, email, id);
    }
}
