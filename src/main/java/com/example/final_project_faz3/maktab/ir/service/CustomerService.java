package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import com.example.final_project_faz3.maktab.ir.data.repository.CustomerRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.CreditNotEnoughException;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.OrderExistenceException;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import com.example.final_project_faz3.maktab.ir.util.validation.sort.MySort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CommentService commentService;
    private final ExpertService expertService;

    @Autowired

    public CustomerService(CustomerRepository customerRepository, CommentService commentService, ExpertService expertService) {
        this.customerRepository = customerRepository;
        this.commentService = commentService;
        this.expertService = expertService;

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
    public void payOrderByCustomer(Customer customer, Orders orders, Expert expert) throws CreditNotEnoughException {
        Long credit = customer.getCredit().getBalance();
        Long proposedPrice = orders.getProposedPrice();
        if (credit > proposedPrice) {
            long l = credit - proposedPrice;
            customer.getCredit().setBalance(l);
            expert.getCredit().setBalance(proposedPrice + expert.getCredit().getBalance());
            orders.setOrderStatus(OrderStatus.PAID);
        }
        throw new CreditNotEnoughException("credit not enough!!");
    }


    public void saveComment(Comment comment, Long expertId, Long subSerViceId) throws ExpertExistenceException {
//        Optional<Customer> customerById = customerService.findCustomerById(customerId);
        Expert expert = expertService.findExpertById(expertId);
        //     if (expert.getSubServiceList().stream().anyMatch(subservice -> subservice.getId().equals(subSerViceId))) {
        //if (customerById.get().getOrdersList().stream().anyMatch(orders -> orders.getSubService().getId().equals(subSerViceId))) {
        expert.setExpertScore(comment.getExpertScore());
        expert.getCommentList().add(comment);
        commentService.saveComment(comment);
        //   }

        //    }
    }

    @Transactional
    public void changePassword(Long customerId, String password) {
        Optional<Customer> customerById = findCustomerById(customerId);
        if (customerById.isPresent()) {
            Validation.validatePassword(password);
            customerById.get().setPassword(password);
        }
    }

    public List<Offers> getOfferList(Long customerId, Long orderId, MySort comparator) {
        Optional<Customer> customerById = findCustomerById(customerId);
        List<Offers> offersList = new ArrayList<>();
        if (customerById.isPresent()) {
            List<Orders> ordersList = customerById.get().getOrdersList();
            boolean anyMatch = ordersList.stream().anyMatch(order -> order.getId().equals(orderId));
            if (anyMatch) {
                int i = Math.toIntExact(orderId);
                Orders orders = ordersList.get(i - 1);
                offersList.addAll(orders.getOffersList());
                offersList.sort(comparator);
                return offersList;
            }
        }
        return offersList;
    }

    @Transactional
    public void chooseOffer(Optional<Orders> orders, Optional<Offers> offers) throws OrderExistenceException {

        Customer customer = orders.get().getCustomer();
        List<Orders> ordersList = customer.getOrdersList();
        Orders orders1 = new Orders();
        for (Orders value : ordersList) {
            if (value.getId().equals(orders.get().getId())) {
                orders1 = value;
                break;
            }
        }
        if (!(orders1.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_PROPOSE)
                || orders1.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_SELECTION)))
            throw new OrderExistenceException("order status not correct!");
        boolean b = orders1.getOffersList().stream().anyMatch(ofr -> ofr.getExpert().equals(offers.get().getExpert()));
        if (b) {
            orders1.setOrderStatus(OrderStatus.WAITING_EXPERT_COMING);
        }
    }
}
