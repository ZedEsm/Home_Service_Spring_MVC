package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Expert;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Offers;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Orders;
import com.example.final_project_faz3.maktab.ir.data.repository.ExpertRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.ExpertExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.OrderExistenceException;
import com.example.final_project_faz3.maktab.ir.service.ExpertService;
import com.example.final_project_faz3.maktab.ir.service.OfferService;
import com.example.final_project_faz3.maktab.ir.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertService expertService;
    private final ExpertRepository expertRepository;
    private final OrderService orderService;

    private final OfferService offerService;

    @Autowired
    public ExpertController(ExpertService expertService,
                            ExpertRepository expertRepository, OrderService orderService, OfferService offerService) {
        this.expertService = expertService;
        this.expertRepository = expertRepository;
        this.orderService = orderService;
        this.offerService = offerService;
    }

    @PostMapping("/postExpert")
    public void registerExpert(@RequestBody Expert expert) {
        try {
            expertService.saveExpert(expert);
        } catch (ExpertExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/postOffer/{expertId}")
    public void submitOffer(@PathVariable Long expertId, @RequestBody Offers offers, @RequestParam(required = false) Long orderId) throws ExpertExistenceException, OrderExistenceException {
        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ExpertExistenceException("expert not found"));
        Orders orders = orderService.findOrderById(orderId).orElseThrow(() -> new OrderExistenceException("order not found"));
        expertService.addOffer(offers, expert, orders);
    }

    @GetMapping("/expertDone/{orderId}")
    public void expertDone(@PathVariable Long orderId, @RequestParam(required = false) Long offerId) {
        try {
            Optional<Orders> orders = Optional.ofNullable(orderService.findOrderById(orderId).orElseThrow(() -> new OrderExistenceException("order does not exist")));
            Optional<Offers> offers = offerService.findOfferById(offerId);
            expertService.expertDone(orders, offers);
        } catch (OrderExistenceException e) {
            System.out.println(e.getMessage());
        }


    }
}
