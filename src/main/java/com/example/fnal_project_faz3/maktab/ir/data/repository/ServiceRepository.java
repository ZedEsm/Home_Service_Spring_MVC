package com.example.fnal_project_faz3.maktab.ir.data.repository;

import com.example.fnal_project_faz3.maktab.ir.data.model.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services,Long> {
}
