package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Services;
import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import com.example.final_project_faz3.maktab.ir.data.repository.ServiceRepository;
import com.example.final_project_faz3.maktab.ir.data.repository.SubServiceRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.ServiceExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.SubServiceExistenceException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubServicesService {
    private final SubServiceRepository subServiceRepository;
    private final ServiceRepository serviceRepository;

    @Autowired

    public SubServicesService(SubServiceRepository subServiceRepository, ServiceRepository serviceRepository) {
        this.subServiceRepository = subServiceRepository;
        this.serviceRepository = serviceRepository;
    }

    public void saveSubService(SubService subService) {
        subServiceRepository.save(subService);
    }

    public void checkSubServiceExistence(SubService subService) throws SubServiceExistenceException, ServiceExistenceException {
        Optional<SubService> byName = subServiceRepository.findByName(subService.getName());
        if (byName.isPresent()) {
            throw new SubServiceExistenceException("this subservice exist!");
        }
        Optional<Services> byServiceName = serviceRepository.findByServiceName(subService.getName());
        if (byServiceName.isPresent()) {
            throw new ServiceExistenceException("this service exist!");
        }

    }

    @Transactional
    public void updateDescription(String name,String description) throws SubServiceExistenceException {
        SubService subService = subServiceRepository.findByName(name).orElseThrow(() -> new SubServiceExistenceException("this subservice does not exist!"));
        subService.setDescription(description);
    }
    @Transactional
    public void updatePrice(String name,int price) throws SubServiceExistenceException {
        SubService subService = subServiceRepository.findByName(name).orElseThrow(() -> new SubServiceExistenceException("this subservice does not exist!"));
        subService.setBasePrice(price);
    }
}
