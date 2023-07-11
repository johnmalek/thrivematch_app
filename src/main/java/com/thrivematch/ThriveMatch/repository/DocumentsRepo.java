package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.DocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentsRepo extends JpaRepository<DocumentsEntity, Integer> {
    boolean existsByName(MultipartFile file);
}
