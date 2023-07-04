package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.dto.UserProfileDto;
import com.thrivematch.ThriveMatch.model.UserProfileEntity;
import com.thrivematch.ThriveMatch.repository.UserProfileRepo;
import com.thrivematch.ThriveMatch.service.UserProfileService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileRepo userProfileRepo;

    @PostMapping("/profiles")
    public ResponseEntity<SuccessAndMessage> createProfile(@RequestPart("name") String name, @RequestPart("pictureFile" ) MultipartFile file){
        SuccessAndMessage response = new SuccessAndMessage();
        try{
            String picturePath = userProfileService.saveFile(file);
            UserProfileEntity profile = new UserProfileEntity();
            profile.setName(name);
            profile.setPicturePath(picturePath);
            UserProfileEntity savedProfile = userProfileRepo.save(profile);
            response.setSuccess(true);
            response.setMessage("Profile created successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e){
            response.setSuccess(false);
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
