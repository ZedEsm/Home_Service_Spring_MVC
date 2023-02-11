package com.example.fnal_project_faz3.maktab.ir.data.repository;

import com.example.fnal_project_faz3.maktab.ir.data.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Long> {
}
