package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Customer;
import com.example.final_project_faz3.maktab.ir.data.repository.CustomerRepository;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer){
        Validation.validateEmail(customer.getEmailAddress());
        Validation.validatePassword(customer.getPassword());
        customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerById(Long id){
        return customerRepository.findById(id);
    }
}
