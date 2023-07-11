package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.InvestorDetails;
import com.thrivematch.ThriveMatch.dto.InvestorInfoResponse;
import com.thrivematch.ThriveMatch.dto.InvestorDetails;
import com.thrivematch.ThriveMatch.dto.InvestorInfoResponse;
import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvestorService {
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;

    public ResponseEntity<InvestorInfoResponse> getAllInvestors(){
        ArrayList<InvestorEntity> investors = new ArrayList<>(investorRepo.findAll());
        ArrayList<IndividualInvestorEntity> individualInvestors = new ArrayList<>(individualInvestorRepo.findAll());
        InvestorInfoResponse investorInfoResponse = new InvestorInfoResponse();
        ArrayList<InvestorDetails> investorDetails = new ArrayList<>();
        if(investors.size() > 0){
            investorInfoResponse.setSuccess(true);
            investorInfoResponse.setMessage("All investors");
            InvestorDetails investorDetail;
            for(InvestorEntity investor: investors){
                investorDetail = new InvestorDetails();
                investorDetail.setName(investor.getName());
                investorDetail.setIndustry(investor.getIndustry());
                investorDetail.setDescription(investor.getDescription());
                investorDetail.setPicturePath(investor.getPicturePath());
                investorDetails.add(investorDetail);
            }
            for(IndividualInvestorEntity individualInvestor: individualInvestors){
                investorDetail = new InvestorDetails();
                investorDetail.setName(individualInvestor.getName());
                investorDetail.setDescription(individualInvestor.getDescription());
                investorDetail.setIndustry(individualInvestor.getIndustry());
                investorDetail.setPicturePath(individualInvestor.getPicturePath());
                investorDetails.add(investorDetail);
            }

            investorInfoResponse.setInvestors(investorDetails);
            return ResponseEntity.ok().body(investorInfoResponse);
        }
        investorInfoResponse.setSuccess(false);
        investorInfoResponse.setMessage("No investors found");
        return ResponseEntity.badRequest().body(investorInfoResponse);
    }
}
