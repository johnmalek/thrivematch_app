package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepo extends JpaRepository<LikesEntity, Long> {
    List<LikesEntity> findByInvestor(InvestorEntity investor);
    List<StartUpEntity> findByStartup(StartUpEntity startUp);
    LikesEntity findByInvestorAndStartup(InvestorEntity investor, StartUpEntity startUp);
}
