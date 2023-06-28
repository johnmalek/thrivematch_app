package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<AdminEntity, Integer> {
    Optional<AdminEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
