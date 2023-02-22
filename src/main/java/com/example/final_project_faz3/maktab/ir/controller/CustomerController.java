package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Customer;
import com.example.final_project_faz3.maktab.ir.service.CreditService;
import com.example.final_project_faz3.maktab.ir.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CreditService creditService;

    @Autowired
    public CustomerController(CustomerService customerService, CreditService creditService) {
        this.customerService = customerService;
        this.creditService = creditService;
    }

    @PostMapping("/postCustomer")
    public void registerCustomer(@RequestBody Customer customer) {
       customerService.saveCustomer(customer);
    }
}
