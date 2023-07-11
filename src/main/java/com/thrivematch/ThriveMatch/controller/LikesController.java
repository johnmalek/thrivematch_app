//package com.thrivematch.ThriveMatch.controller;
//
//import com.thrivematch.ThriveMatch.model.InvestorEntity;
//import com.thrivematch.ThriveMatch.model.LikesEntity;
//import com.thrivematch.ThriveMatch.model.StartUpEntity;
//import com.thrivematch.ThriveMatch.repository.InvestorRepo;
//import com.thrivematch.ThriveMatch.repository.LikesRepo;
//import com.thrivematch.ThriveMatch.repository.StartUpRepo;
//import com.thrivematch.ThriveMatch.service.LikeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/v1")
//public class LikesController {
//    @Autowired
//    private LikeService likeService;
//    @Autowired
//    private InvestorRepo investorRepo;
//    @Autowired
//    private StartUpRepo startUpRepo;
//    @Autowired
//    private LikesRepo likesRepo;
//
//    @PostMapping("/investors/{investorId}/startup/{startupId}/like")
//    public ResponseEntity<String> likeStartUp(@PathVariable Integer investorId, @PathVariable Integer startupId){
//        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
//        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
//
//        if(investor.isEmpty() || startUp.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        likeService.saveLikes(investor.get(), startUp.get());
//        return ResponseEntity.ok("Startup liked successfully");
//    }
//
//    @DeleteMapping("/investors/{investorId}/startup/{startupId}/unlike")
//    public ResponseEntity<String> unLikeStartUp(@PathVariable Integer investorId, @PathVariable Integer startupId){
//        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
//        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
//
//        if(investor.isEmpty() || startUp.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        likeService.deleteLikes(investor.get(), startUp.get());
//        return ResponseEntity.ok("Startup unliked successfully");
//    }
//}
