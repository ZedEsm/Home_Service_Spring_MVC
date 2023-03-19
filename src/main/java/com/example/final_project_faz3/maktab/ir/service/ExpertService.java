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

import java.time.Duration;
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
        expert.setPerformance(0);
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
        calculateExpertDelayTime(offers, Optional.of(orders1));
        System.out.println("zeddddddddd");


    }
    @Transactional
    private void calculateExpertDelayTime(Optional<Offers> offers,Optional<Orders>orders) {
        Date start = offers.get().getTimeToStartWork();
        Date finish = offers.get().getDate();

        Duration duration = Duration.between(start.toInstant(), finish.toInstant());

        orders.get().setDuration(Duration.ZERO.plusDays(1).plusHours(1).plusMinutes(30));
        long hours = orders.get().getDuration().minus(duration).toHours();
        System.out.println(hours);
//        if (hours >= 0)
//            return;


        double averageScore = offers.get().getExpert().getPerformance() - Math.abs(hours);//TODO:get expert score
        offers.get().getExpert().setPerformance(averageScore);
        System.out.println(averageScore);
        if (averageScore < 0) {
            offers.get().getExpert().setExpertStatus(ExpertStatus.PENDING_CONFIRMATION);
            offers.get().getExpert().setEnabled(false);
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
    public void pay(Expert expert, Long proposedPrice) {
        double v = expert.getCredit().getBalance() + 0.7 * proposedPrice;
        expert.getCredit().setBalance((long) v);
    }
}
