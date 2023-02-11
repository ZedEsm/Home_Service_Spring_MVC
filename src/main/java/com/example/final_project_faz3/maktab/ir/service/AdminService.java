package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Admin;
import com.example.final_project_faz3.maktab.ir.data.repository.AdminRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.AdminExistenceException;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import jakarta.transaction.Transactional;
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

    public void deleteAdminById(Integer id) throws AdminExistenceException {
        boolean exists = adminRepository.existsById(id);
        if (!exists) {
            throw new AdminExistenceException("admin with id " + id + " does not exists");
        }
        adminRepository.deleteById(id);
    }

    @Transactional
    public void updateAdminPasswordById(Integer id, String newPassword) throws AdminExistenceException {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new AdminExistenceException("admin does not exist"));
        Validation.validatePassword(newPassword);
        admin.setPassword(newPassword);

    }

}
