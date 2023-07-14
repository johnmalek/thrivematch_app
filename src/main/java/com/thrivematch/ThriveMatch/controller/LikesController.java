package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import com.thrivematch.ThriveMatch.repository.LikesRepo;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import com.thrivematch.ThriveMatch.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class LikesController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private LikesRepo likesRepo;

    // Investor like startup
    @PostMapping("/investors/{investorId}/startup/{startupId}/like")
    public ResponseEntity<SuccessAndMessage> likeStartUp(@PathVariable Integer investorId, @PathVariable Integer startupId){
        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
        SuccessAndMessage response = new SuccessAndMessage();

        if(investor.isEmpty() || startUp.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Investor or startup does noy exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(likeService.isStartupLikedByInvestor(investor.get(), startUp.get())){
            response.setSuccess(false);
            response.setMessage("Startup already liked");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        likeService.saveLikes(investor.get(), startUp.get());
        response.setSuccess(false);
        response.setMessage("Startup liked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Investor unlike startup
    @DeleteMapping("/investors/{investorId}/startup/{startupId}/unlike")
    public ResponseEntity<SuccessAndMessage> unLikeStartUp(@PathVariable Integer investorId, @PathVariable Integer startupId){
        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
        SuccessAndMessage response = new SuccessAndMessage();

        if(investor.isEmpty() || startUp.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Investor or startup does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        likeService.deleteLikes(investor.get(), startUp.get());
        response.setSuccess(false);
        response.setMessage("Startup unliked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Startup like an investor
    @PostMapping("/startups/{startupId}/investors/{investorId}/like")
    public ResponseEntity<SuccessAndMessage> likeInvestor(@PathVariable Integer investorId, @PathVariable Integer startupId){
        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
        SuccessAndMessage response = new SuccessAndMessage();

        if(investor.isEmpty() || startUp.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Investor or startup does noy exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(likeService.isInvestorLikedByStartup(startUp.get(), investor.get())){
            response.setSuccess(false);
            response.setMessage("Investor already liked");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        likeService.saveLikes(investor.get(), startUp.get());
        response.setSuccess(false);
        response.setMessage("Investor liked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Startup unlike an investor
    @DeleteMapping("/startups/{startupId}/investors/{investorId}/unlike")
    public ResponseEntity<SuccessAndMessage> unLikeInvestor(@PathVariable Integer investorId, @PathVariable Integer startupId){
        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
        SuccessAndMessage response = new SuccessAndMessage();

        if(investor.isEmpty() || startUp.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Investor or startup does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        likeService.deleteLikes(investor.get(), startUp.get());
        response.setSuccess(false);
        response.setMessage("Investor unliked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}