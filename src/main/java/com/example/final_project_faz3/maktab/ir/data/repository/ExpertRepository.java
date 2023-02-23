package com.example.final_project_faz3.maktab.ir.data.repository;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Optional<Expert> findExpertByEmailAddress(String emailAddress);

    Optional<Expert> findExpertById(Long id);
}
