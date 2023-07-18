package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualInvestorRepo extends JpaRepository<IndividualInvestorEntity, Integer> {
    boolean existsByName(String name);
}
