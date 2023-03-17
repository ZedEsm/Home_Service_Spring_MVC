package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertStatus;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import com.example.final_project_faz3.maktab.ir.data.repository.ExpertRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.OrderExistenceException;
import com.example.final_project_faz3.maktab.ir.util.validation.Validation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExpertService {
    private final ExpertRepository expertRepository;
    private final OfferService offerService;

    @Autowired
    public ExpertService(ExpertRepository expertRepository, OfferService offerService) {
        this.expertRepository = expertRepository;

        this.offerService = offerService;
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

    public Expert findExpertById(Long id) throws ExpertExistenceException {
        return expertRepository.findById(id).orElseThrow(() -> new ExpertExistenceException("expert does not exist!"));
    }

    @Transactional
    public void updateExpertById(Long id, SubService subService) throws ExpertExistenceException {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new ExpertExistenceException("expert does not exist!"));
        expert.getSubServiceList().add(subService);
    }

    public void addOffer(Offers offers, Expert expert, Orders orders) {

        if (expert.getSubServiceList().stream().anyMatch(sub -> sub.getName().equals(orders.getSubService().getName()))) {
            if (checkExpertStatus(orders)) {
                if (offers.getProposedPrice() > orders.getSubService().getBasePrice()) {
                    orders.setOrderStatus(OrderStatus.WAITING_EXPERT_SELECTION);
                    orders.getOffersList().add(offers);
                    offers.setExpert(expert);
                    offerService.saveOffer(offers);
                }

            }
        }
    }

    private boolean checkExpertStatus(Orders orders) {
        return orders.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_PROPOSE) | orders.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_SELECTION);
    }
    @Transactional

    public void expertDone(Optional<Orders> orders, Optional<Offers> offers) throws OrderExistenceException {
        Orders orders1 = checkOrder(orders);
        if (!orders1.getOrderStatus().equals(OrderStatus.STARTING))
            throw new OrderExistenceException("order not started!");

        offers.get().setDate(new Date());
        orders1.setOrderStatus(OrderStatus.DONE);
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
}
