package com.example.fnal_project_faz3.maktab.ir.data.repository;

import com.example.fnal_project_faz3.maktab.ir.data.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
