package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Admin;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Expert;
import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertStatus;
import com.example.final_project_faz3.maktab.ir.data.repository.AdminRepository;
import com.example.final_project_faz3.maktab.ir.data.repository.ExpertRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.AdminExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertConfirmationException;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.SubServiceExistenceException;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final SubServicesService subServicesService;

    private final ExpertService expertService;
    private final AdminRepository adminRepository;
    private final ExpertRepository expertRepository;

    public AdminService(SubServicesService subServicesService, ExpertService expertService, AdminRepository adminRepository,
                        ExpertRepository expertRepository) {
        this.subServicesService = subServicesService;
        this.expertService = expertService;
        this.adminRepository = adminRepository;
        this.expertRepository = expertRepository;
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

    public void addExpertToSubService(Long exId, Long subId) throws ExpertConfirmationException, SubServiceExistenceException, ExpertExistenceException {
        Expert expertById = expertService.findExpertById(exId);
        Optional<SubService> subServiceById = subServicesService.findSubServiceById(subId);
        if (expertById.getExpertStatus().equals(ExpertStatus.NEW)) {
            throw new ExpertConfirmationException("this expert does not confirmed yet by admin!");
        }
        if (expertById.getSubServiceList().stream().anyMatch(subService -> subService.getName().equals(subServiceById.get().getName()))) {
            throw new SubServiceExistenceException("this subservice already exist!");
        }
        expertService.updateExpertById(expertById.getId(), subServiceById.get());
    }

    public void deleteExpertFromSubservice(Long id) throws ExpertExistenceException {
        Optional<Expert> expert = Optional.ofNullable(expertRepository.findExpertById(id).orElseThrow(() -> new ExpertExistenceException("this expert does not exist!")));
        if (expert.isPresent()) {
            expertRepository.deleteById(id);
        }
    }

    @Transactional
    public void confirmExpertStatus(Long expertId) throws ExpertExistenceException {
        Expert expertById = expertService.findExpertById(expertId);
        if (expertById.getExpertStatus().equals(ExpertStatus.NEW)) {
            expertById.setExpertStatus(ExpertStatus.BEEN_CONFIRMED);
        }

    }

}
