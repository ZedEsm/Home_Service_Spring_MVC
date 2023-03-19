package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.dto.CreditPaymentDto;
import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.PaymentType;
import com.example.final_project_faz3.maktab.ir.data.repository.CustomerRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.*;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import com.example.final_project_faz3.maktab.ir.util.sort.MySort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CommentService commentService;
    private final ExpertService expertService;

    private final OfferService offerService;


    @Autowired

    public CustomerService(CustomerRepository customerRepository, CommentService commentService, ExpertService expertService, OfferService offerService) {
        this.customerRepository = customerRepository;
        this.commentService = commentService;
        this.expertService = expertService;
        this.offerService = offerService;

    }

    public void saveCustomer(Customer customer) {
        Validation.validateEmail(customer.getEmailAddress());
        Validation.validatePassword(customer.getPassword());
        customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmailAddress(email);
    }

//    @Transactional
//    public void payOrderByCustomer(Customer customer, Orders orders, Expert expert) throws CreditNotEnoughException {
//        Long credit = customer.getCredit().getBalance();
//        Long proposedPrice = orders.getProposedPrice();
//        if (credit > proposedPrice) {
//            long l = credit - proposedPrice;
//            customer.getCredit().setBalance(l);
//            expert.getCredit().setBalance(proposedPrice + expert.getCredit().getBalance());
//            orders.setOrderStatus(OrderStatus.PAID);
//        }
//        throw new CreditNotEnoughException("credit not enough!!");
//    }


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

        Orders orders1 = checkOrder(orders);
        if (!(orders1.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_PROPOSE)
                || orders1.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_SELECTION)))
            throw new OrderExistenceException("order status not correct!");
        boolean b = orders1.getOffersList().stream().anyMatch(ofr -> ofr.getExpert().equals(offers.get().getExpert()));
        if (b) {
            orders1.setOrderStatus(OrderStatus.WAITING_EXPERT_COMING);
        }
    }

    @Transactional

    public void changeOrderStatus(Optional<Orders> orders, Optional<Offers> offers) throws OrderExistenceException, TimeAfterException {

        Orders orders1 = checkOrder(orders);
        if (!(orders1.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_COMING)))
            throw new OrderExistenceException("order status not correct!");
        boolean anyMatch = orders1.getOffersList().stream().anyMatch(ofr -> ofr.getExpert().equals(offers.get().getExpert()));
        if (anyMatch) {
            Date now = new Date();
            if (offers.get().getTimeToStartWork().after(now))
                throw new TimeAfterException("time is after exception");
            orders1.setOrderStatus(OrderStatus.STARTING);
            offers.get().setTimeToStartWork(now);
        }
    }

    private Orders checkOrder(Optional<Orders> orders) {
        Customer customer = orders.get().getCustomer();
        List<Orders> ordersList = customer.getOrdersList();
        Orders orders1 = new Orders();
        for (Orders value : ordersList) {
            if (value.getId().equals(orders.get().getId())) {
                orders1 = value;
                break;
            }
        }
        return orders1;
    }

    @Transactional
    public void pay(Optional<Customer> customer, Long payment) throws CreditNotEnoughException {
        Long creditAmount = customer.get().getCredit().getBalance();
        if (creditAmount < payment)
            throw new CreditNotEnoughException("credit not enough");
        customer.get().getCredit().setBalance(creditAmount - payment);
    }

    @Transactional
    public void payOrderFromCredit(CreditPaymentDto creditPaymentDto, Optional<Orders> orders) throws CustomerNotFoundException, OrderExistenceException, CreditNotEnoughException {
        Optional<Customer> customer = Optional.ofNullable(findCustomerByEmail(creditPaymentDto.getCustomerEmail()).orElseThrow(() -> new CustomerNotFoundException("customer not found !")));
        if (!orders.get().getOrderStatus().equals(OrderStatus.DONE))
            throw new OrderExistenceException("order not done yet!");
        System.out.println(creditPaymentDto.getOfferId());
        Optional<Offers> offerById = Optional.ofNullable(offerService.findOfferById(creditPaymentDto.getOfferId()).orElseThrow(() -> new OrderExistenceException("offer not found")));

        Long proposedPrice = offerById.get().getProposedPrice();
        if (creditPaymentDto.getPaymentType().equals(PaymentType.CREDIT)) {
            pay(customer, proposedPrice);
        }
        Expert expert = offerById.get().getExpert();
        expertService.pay(expert, proposedPrice);
        orders.get().setProposedPrice(proposedPrice);
        orders.get().setOrderStatus(OrderStatus.PAID);
    }

    @Transactional
    public void addComment(Comment comment, Optional<Orders> orderById) throws OrderExistenceException {
        commentService.saveComment(comment);
        if (!orderById.get().getOrderStatus().equals(OrderStatus.PAID))
            throw new OrderExistenceException("order not payed!");
        Expert expert = comment.getExpert();
        expert.getCommentList().add(comment);
        orderById.get().setOrderStatus(OrderStatus.SCORED);
        expert.setPerformance(comment.getExpert().getPerformance());
        comment.setExpertScore(comment.getExpert().getExpertScore());
        expert.setExpertScore(comment.getExpert().getExpertScore());

    }

}
