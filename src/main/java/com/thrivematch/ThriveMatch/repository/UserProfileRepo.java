package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, Integer> {
    Optional<UserProfileEntity> findImageById(Integer integer);
}
