package com.thrivematch.ThriveMatch.dto;

public class LikesDTO {
    private String name;
    private String picturePath;

    public LikesDTO(String name, String picturePath) {
        this.name = name; this.picturePath = picturePath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
