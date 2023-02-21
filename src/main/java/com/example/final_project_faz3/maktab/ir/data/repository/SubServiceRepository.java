package com.example.final_project_faz3.maktab.ir.data.repository;

import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubServiceRepository extends JpaRepository<SubService, Long> {
    Optional<SubService> findByName(String name);
}
