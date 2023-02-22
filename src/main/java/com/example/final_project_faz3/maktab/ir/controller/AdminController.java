package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Admin;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Services;
import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import com.example.final_project_faz3.maktab.ir.exceptions.AdminExistenceException;
import com.example.final_project_faz3.maktab.ir.service.AdminService;
import com.example.final_project_faz3.maktab.ir.service.ServicesService;
import com.example.final_project_faz3.maktab.ir.service.SubServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;
    private final ServicesService servicesService;
    private final SubServicesService subServicesService;

    @Autowired
    public AdminController(AdminService adminService, ServicesService servicesService,
                           SubServicesService subServicesService) {
        this.adminService = adminService;
        this.servicesService = servicesService;
        this.subServicesService = subServicesService;
    }

    @GetMapping("/getAdmin/{username}")
    public Optional<Admin> getAdmin(@PathVariable String username) {
        return adminService.getAdmin(username);
    }

    @PostMapping("/postAdmin")
    public void registerAdmin(@RequestBody Admin admin) {
        try {
            adminService.registerAdmin(admin);
        } catch (AdminExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

    @PutMapping(path = "/update/{adminId}")
    public void updatePassword(@PathVariable("adminId") Integer id,
                               @RequestParam(required = false) String password) throws AdminExistenceException {
        adminService.updateAdminPasswordById(id, password);
    }

    @GetMapping("/getServices")
    public List<Services> getServices() {
        return servicesService.getAllServices();
    }

    @DeleteMapping(path = "/delete/{adminId}")
    public void deleteAdminByID(@PathVariable("adminId") Integer id) {
        try {
            adminService.deleteAdminById(id);
        } catch (AdminExistenceException e) {
            System.out.println(e.getMessage());
        }
    }
    @GetMapping("/getSubServices")
    public List<SubService> getSubServices() {
        return subServicesService.getAllSubServices();
    }
}
