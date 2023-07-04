package com.thrivematch.ThriveMatch.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileDto {
    private String name;
    private MultipartFile pictureFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(MultipartFile pictureFile) {
        this.pictureFile = pictureFile;
    }
}
