package com.example.final_project_faz3.maktab.ir.data.repository;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    Optional<Services> findByServiceName(String name);
}
