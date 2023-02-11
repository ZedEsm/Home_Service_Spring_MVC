package com.example.fnal_project_faz3.maktab.ir.data.repository;


import com.example.fnal_project_faz3.maktab.ir.data.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Optional<Admin>findByUserName(String username);
}
