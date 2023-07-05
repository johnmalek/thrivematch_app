package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<AdminEntity, Integer> {
    Optional<AdminEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
