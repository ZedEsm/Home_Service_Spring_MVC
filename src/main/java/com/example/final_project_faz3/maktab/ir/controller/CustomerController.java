package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.dto.OffersDto;
import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.exceptions.*;
import com.example.final_project_faz3.maktab.ir.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")

public class CustomerController {
    private final ModelMapper mapper;
    private final CustomerService customerService;

    private final SubServicesService subServicesService;

    private final ServicesService servicesService;

    private final OrderService orderService;

    private final AddressService addressService;

    private final ExpertService expertService;


    @Autowired
    public CustomerController(ModelMapper mapper, CustomerService customerService, SubServicesService subServicesService, ServicesService servicesService, OrderService orderService, AddressService addressService, ExpertService expertService) {
        this.mapper = mapper;
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
            customerService.payOrderByCustomer(customer, order, expert);

        } catch (CustomerNotFoundException | OrderExistenceException | ExpertExistenceException |
                 CreditNotEnoughException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/addComment/{expertId}")
    public void addComment(@RequestBody Comment comment, @PathVariable Long expertId, @RequestParam Long subserviceId) {
        try {
            customerService.saveComment(comment, expertId, subserviceId);
        } catch (ExpertExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

    @PutMapping(path = "/changePassword/{customerId}")
    public void changePassword(@PathVariable("customerId") Long customerId, @RequestParam(required = false) String password) {
        customerService.changePassword(customerId, password);
    }

    @GetMapping("/getOfferList/{customerId}")

    public List<OffersDto> getOfferList(@PathVariable Long customerId, @RequestParam(required = false) Long orderId) {
        List<Offers> offerList = customerService.getOfferList(customerId, orderId);
        List<OffersDto> offersDtoList = new ArrayList<>();
        for (Offers offers:
             offerList) {
            offersDtoList.add(new OffersDto(offers.getDate(),offers.getProposedPrice(),offers.getTimeToStartWork(),offers.getExpert().getId(),offers.getExpert().getFirstName()));

        }
        return offersDtoList;
    }
//    @GetMapping("/test")
//    public UserDto getCustomer(){
//        Customer customer = new Customer("f", "f", "zed", "ewa");
//
//        List<OfferDto> offerDto = new ArrayList<>();
//        offerDto.add(new OfferDto("aze"));
//        offerDto.add(new OfferDto("azce"));
//        offerDto.add(new OfferDto("azcce"));
//
//       // UserDto userDto = new UserDto(customer.getFirstName(),customer.getLastName(),customer.getEmailAddress(),offerDto);
//        UserDto userDto = mapper.map(customer,UserDto.class);
//        return userDto;
//    }

}
//@AllArgsConstructor
//@Getter
//@Setter
//@NoArgsConstructor
//class UserDto{
//    String firstName;
//    String lastName;
//    String emailAddress;
//    List<OfferDto> offerDto;
//}
//@AllArgsConstructor
//
//@Getter
//@Setter
//class OfferDto{
//    String name;
//
//}
