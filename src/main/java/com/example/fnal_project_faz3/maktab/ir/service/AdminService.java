package com.example.fnal_project_faz3.maktab.ir.service;

import com.example.fnal_project_faz3.maktab.ir.data.model.entity.Admin;
import com.example.fnal_project_faz3.maktab.ir.data.repository.AdminRepository;
import com.example.fnal_project_faz3.maktab.ir.exceptions.AdminExistenceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void registerAdmin(Admin admin) throws AdminExistenceException {
        if (getAdmin(admin.getUserName()).isEmpty()) {
            adminRepository.save(admin);
        }
        throw new AdminExistenceException("admin with this username exist!");
    }

    public Optional<Admin> getAdmin(String username) {
        return adminRepository.findByUserName(username);
    }
}
