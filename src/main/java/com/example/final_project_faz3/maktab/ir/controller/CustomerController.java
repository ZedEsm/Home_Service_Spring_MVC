package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.exceptions.*;
import com.example.final_project_faz3.maktab.ir.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//todo:credit add to customer
@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    private final SubServicesService subServicesService;

    private final ServicesService servicesService;

    private final OrderService orderService;

    private final AddressService addressService;

    private final ExpertService expertService;



    @Autowired
    public CustomerController(CustomerService customerService, SubServicesService subServicesService, ServicesService servicesService, OrderService orderService, AddressService addressService, ExpertService expertService) {
        this.customerService = customerService;
        this.subServicesService = subServicesService;
        this.servicesService = servicesService;
        this.orderService = orderService;
        this.addressService = addressService;
        this.expertService = expertService;
    }

    @PostMapping("/post")
    public void registerCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
    }

    @GetMapping("/getSubServices")
    public List<SubService> getSubServices() {
        return subServicesService.getAllSubServices();
    }

    @GetMapping("/getServices")
    public List<Services> getServices() {
        return servicesService.getAllServices();
    }

    //    @PostMapping("/addCredit/{customId}")
//    public void addExpertToSubService(@PathVariable Long customId,
//                                      @RequestParam(required = false) Long subId) {
//        try {
//            adminService.addExpertToSubService(exId, subId);
//        } catch (ExpertConfirmationException | SubServiceExistenceException | ExpertExistenceException e) {
//            System.out.println(e.getMessage());
//        }
//    }
    @PostMapping("/postOrder/{customerId}")
    public void submitOrder(@RequestBody Orders orders, @PathVariable Long customerId, @RequestParam(required = false) Long subId) {
        try {
            orderService.submitOrder(customerId, orders, subId);
        } catch (OrderExistenceException | OrderRegistrationFailedException | UnTimeOrderException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/postAddress")
    public void postAddress(@RequestBody Address address) {
        addressService.saveAddress(address);
    }

    @PutMapping(path = "/payOrder/{customerId}")
    public void payOrder(@PathVariable("customerId") Long customerId, @RequestParam(required = false) Long expertId, @RequestParam(required = false) Long orderId) {
        try {
            Customer customer = customerService.findCustomerById(customerId).orElseThrow(() -> new CustomerNotFoundException("customer not found exception"));
            Orders order = orderService.findOrderById(orderId).orElseThrow(() -> new OrderExistenceException("order not found"));
            Expert expert = expertService.findExpertById(expertId);
            customerService.payOrderByCustomer(customer,order,expert);

        } catch (CustomerNotFoundException | OrderExistenceException | ExpertExistenceException |
                 CreditNotEnoughException e) {
            System.out.println(e.getMessage());
        }

    }



}
