package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Credit;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Customer;
import com.example.final_project_faz3.maktab.ir.data.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditService {
    private final CreditRepository creditRepository;


    private final CustomerService customerService;


    @Autowired
    public CreditService(CreditRepository creditRepository, CustomerService customerService) {
        this.creditRepository = creditRepository;
        this.customerService = customerService;
    }
    public void saveCredit(Credit credit){
        creditRepository.save(credit);
    }

    public void saveCustomerCredit(Long id,Credit credit){
        Optional<Customer> customer = customerService.findCustomerById(id);
        if(customer.isPresent()){
            customer.get().setCredit(credit);
            creditRepository.save(credit);
        }
    }
}
