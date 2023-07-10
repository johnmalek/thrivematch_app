package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.LikesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesService {
    @Autowired
    private LikesRepo likesRepo;
    @Autowired
    private LikesEntity likesEntity;

    public LikesEntity saveLike(InvestorEntity investor, StartUpEntity startUp){
        LikesEntity like = new LikesEntity();
        like.setInvestor(investor);
        like.setStartUp(startUp);
        return likesRepo.save(like);
    }

    public void deleteLike(InvestorEntity investor, StartUpEntity startUp){
        LikesEntity like = likesRepo.findByInvestorAndStartup(investor, startUp);
        if(like != null) {
            likesRepo.delete(like);
        }
    }

    public boolean isStartupLikedByInvestor(InvestorEntity investor, StartUpEntity startup) {
        return likesRepo.findByInvestorAndStartup(investor, startup) != null;
    }
}
