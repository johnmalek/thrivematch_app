package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.LikesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikesRepo likesRepo;

    public void saveLikes(InvestorEntity investor, StartUpEntity startUp){
        LikesEntity likes = new LikesEntity();
        likes.setInvestor(investor);
        likes.setStartUp(startUp);
        likesRepo.save(likes);
    }

    public void deleteLikes(InvestorEntity investor, StartUpEntity startUp){
        LikesEntity like = likesRepo.findByInvestorAndStartup(investor, startUp);

        if(like != null){
            like.setInvestor(null);
            like.setStartUp(null);
            likesRepo.save(like);

            likesRepo.delete(like);
        }
    }

    public boolean isStartupLikedByInvestor(InvestorEntity investorEntity, StartUpEntity startUpEntity) {
        return likesRepo.findByInvestorAndStartup(investorEntity, startUpEntity) != null;
    }

    public boolean isInvestorLikedByStartup(StartUpEntity startUpEntity, InvestorEntity investorEntity) {
        return likesRepo.findByInvestorAndStartup(investorEntity, startUpEntity) != null;
    }
}
