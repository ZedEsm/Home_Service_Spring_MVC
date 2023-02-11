package com.example.fnal_project_faz3.maktab.ir.controller;

import com.example.fnal_project_faz3.maktab.ir.data.model.entity.Admin;
import com.example.fnal_project_faz3.maktab.ir.exceptions.AdminExistenceException;
import com.example.fnal_project_faz3.maktab.ir.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/getAdmin/{username}")
    public Optional<Admin> getAdmin(@PathVariable String username) {
        return adminService.getAdmin(username);
    }

    @PostMapping("/post")
    public void registerStudent(@RequestBody Admin admin) {
        try {
            adminService.registerAdmin(admin);
        } catch (AdminExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

}
