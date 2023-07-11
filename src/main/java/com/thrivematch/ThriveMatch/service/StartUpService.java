package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.StartUpDetails;
import com.thrivematch.ThriveMatch.dto.StartUpInfoResponse;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StartUpService {
    @Autowired
    private StartUpRepo startUpRepo;

    public ResponseEntity<StartUpInfoResponse> getAllStartups(){
        ArrayList<StartUpEntity> startups = new ArrayList<>(startUpRepo.findAll());
        StartUpInfoResponse startUpInfoResponse = new StartUpInfoResponse();
        ArrayList<StartUpDetails> startUpDetails = new ArrayList<>();
        if(startups.size() > 0){
            startUpInfoResponse.setSuccess(true);
            startUpInfoResponse.setMessage("All startups");
            StartUpDetails startUpDetail;
            for(StartUpEntity startUp: startups){
                startUpDetail = new StartUpDetails();
                startUpDetail.setName(startUp.getName());
                startUpDetail.setIndustry(startUp.getIndustry());
                startUpDetail.setDescription(startUp.getDescription());
                startUpDetail.setPicturePath(startUp.getPicturePath());
                startUpDetails.add(startUpDetail);
            }
            startUpInfoResponse.setStartups(startUpDetails);
            return ResponseEntity.ok().body(startUpInfoResponse);
        }
        startUpInfoResponse.setSuccess(false);
        startUpInfoResponse.setMessage("No startups found");
        return ResponseEntity.badRequest().body(startUpInfoResponse);
    }
}
