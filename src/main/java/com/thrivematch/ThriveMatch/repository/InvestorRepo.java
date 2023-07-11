package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.InvestorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorRepo extends JpaRepository<InvestorEntity, Integer> {
    boolean existsByName(String name);
}
