package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Customer;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Expert;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Orders;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import com.example.final_project_faz3.maktab.ir.data.repository.CustomerRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.CreditNotEnoughException;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        Validation.validateEmail(customer.getEmailAddress());
        Validation.validatePassword(customer.getPassword());
        customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    @Transactional
    public boolean payOrderByCustomer(Customer customer, Orders orders, Expert expert) throws CreditNotEnoughException {
        Long credit = customer.getCredit().getBalance();
        Long proposedPrice = orders.getProposedPrice();

        if (credit > proposedPrice) {
            long l = credit - proposedPrice;
            customer.getCredit().setBalance(l);
            System.out.println(proposedPrice);
            System.out.println(l);
            expert.getCredit().setBalance(proposedPrice + expert.getCredit().getBalance());
            orders.setOrderStatus(OrderStatus.PAID);
            System.out.println(expert.getCredit()+" **** "+customer.getCredit());
            return true;
        }
        throw new CreditNotEnoughException("credit not enough!!");
    }
}
