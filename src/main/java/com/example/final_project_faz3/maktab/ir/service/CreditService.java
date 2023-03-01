package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Credit;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Customer;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Expert;
import com.example.final_project_faz3.maktab.ir.data.repository.CreditRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertExistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditService {
    private final CreditRepository creditRepository;
    private final CustomerService customerService;

    private final ExpertService expertService;

    @Autowired
    public CreditService(CreditRepository creditRepository, CustomerService customerService, ExpertService expertService) {
        this.creditRepository = creditRepository;
        this.customerService = customerService;
        this.expertService = expertService;
    }

    public void saveExpertCredit(Long id,Credit credit) throws ExpertExistenceException {
        Expert expertById = expertService.findExpertById(id);
        expertById.setCredit(credit);
        creditRepository.save(credit);
    }

    public void saveCustomerCredit(Long id, Credit credit) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        if (customer.isPresent()) {
            customer.get().setCredit(credit);
            creditRepository.save(credit);
        }
    }
}
