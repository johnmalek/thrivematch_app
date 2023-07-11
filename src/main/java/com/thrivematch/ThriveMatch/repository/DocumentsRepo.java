package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.DocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentsRepo extends JpaRepository<DocumentsEntity, Integer> {
    boolean existsByFilePath(String filePath);
}
