package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.StartUpEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartUpRepo extends JpaRepository<StartUpEntity, Integer> {
}
