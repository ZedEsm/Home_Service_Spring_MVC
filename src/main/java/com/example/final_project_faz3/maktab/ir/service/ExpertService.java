package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Expert;
import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertStatus;
import com.example.final_project_faz3.maktab.ir.data.repository.ExpertRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertExistenceException;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpertService {
    private final ExpertRepository expertRepository;

    @Autowired
    public ExpertService(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }

    public void saveExpert(Expert expert) throws ExpertExistenceException {
        Optional<Expert> expertByEmailAddress = expertRepository.findExpertByEmailAddress(expert.getEmailAddress());
//        byte[] bytes = ByteStreams.toByteArray(Files.newInputStream(file.toPath()));
        if (expertByEmailAddress.isPresent()) {
            throw new ExpertExistenceException("This Expert Already Exist!");
        }
        Validation.validatePassword(expert.getPassword());
        Validation.validateEmail(expert.getEmailAddress());
//        Validation.validateImageFormat(file);
//        Validation.validatePhotoSize(bytes);
        expert.setExpertScore(ExpertScore.UNCOMMENT_YET);
        expert.setExpertStatus(ExpertStatus.NEW);
//        expert.setPersonalPhoto(bytes);
//        Validation.validateImageFormat(file);
        expertRepository.save(expert);
    }

    public Optional<Expert> findExpertById(Long id){
        return expertRepository.findById(id);
    }

    @Transactional
    public void updateExpertById(Long id, SubService subService) throws ExpertExistenceException {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new ExpertExistenceException("expert does not exist!"));
        expert.getSubServiceList().add(subService);
    }
}
