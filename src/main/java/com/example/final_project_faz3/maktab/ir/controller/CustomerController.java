package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.dto.CreditPaymentDto;
import com.example.final_project_faz3.maktab.ir.data.dto.OffersDto;
import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.PaymentType;
import com.example.final_project_faz3.maktab.ir.exceptions.*;
import com.example.final_project_faz3.maktab.ir.service.*;
import com.example.final_project_faz3.maktab.ir.util.validation.sort.MySort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final OfferService offerService;


    @Autowired
    public CustomerController(ModelMapper mapper, CustomerService customerService, SubServicesService subServicesService, ServicesService servicesService, OrderService orderService, AddressService addressService, ExpertService expertService, OfferService offerService) {
        this.mapper = mapper;
        this.customerService = customerService;
        this.subServicesService = subServicesService;
        this.servicesService = servicesService;
        this.orderService = orderService;
        this.addressService = addressService;
        this.expertService = expertService;
        this.offerService = offerService;
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

//    @PutMapping(path = "/payOrder/{customerId}")
//    public void payOrder(@PathVariable("customerId") Long customerId, @RequestParam(required = false) Long expertId, @RequestParam(required = false) Long orderId) {
//        try {
//            Customer customer = customerService.findCustomerById(customerId).orElseThrow(() -> new CustomerNotFoundException("customer not found exception"));
//            Orders order = orderService.findOrderById(orderId).orElseThrow(() -> new OrderExistenceException("order not found"));
//            Expert expert = expertService.findExpertById(expertId);
//            customerService.payOrderByCustomer(customer, order, expert);
//
//        } catch (CustomerNotFoundException | OrderExistenceException | ExpertExistenceException |
//                 CreditNotEnoughException e) {
//            System.out.println(e.getMessage());
//        }
//    }

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

    @GetMapping("/getOfferList/{customerId}")//TODO:sort offerList by expertScore

    public List<OffersDto> getOfferList(@PathVariable Long customerId, @RequestParam(required = false) Long orderId) {

        MySort comparator = new MySort();

        List<Offers> offerList = customerService.getOfferList(customerId, orderId, comparator);
        List<OffersDto> offersDtoList = new ArrayList<>();
        for (Offers offers :
                offerList) {
            offersDtoList.add(new OffersDto(offers.getDate(), offers.getProposedPrice(), offers.getTimeToStartWork(), offers.getExpert().getId(), offers.getExpert().getFirstName()));

        }
        return offersDtoList;
    }

    @GetMapping("/chooseOffer/{orderId}")
    public void chooseExpertForOrder(@PathVariable Long orderId, @RequestParam(required = false) Long offerId) {
        try {
            Optional<Orders> orders = Optional.ofNullable(orderService.findOrderById(orderId).orElseThrow(() -> new OrderExistenceException("order does not exist")));
            Optional<Offers> offers = offerService.findOfferById(offerId);
            customerService.chooseOffer(orders, offers);
        } catch (OrderExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/changeOrderStatus/{orderId}")
    public void changeOrderStatus(@PathVariable Long orderId, @RequestParam(required = false) Long offerId) {
        try {
            Optional<Orders> orders = Optional.ofNullable(orderService.findOrderById(orderId).orElseThrow(() -> new OrderExistenceException("order does not exist")));
            Optional<Offers> offers = offerService.findOfferById(offerId);
            customerService.changeOrderStatus(orders, offers);
        } catch (OrderExistenceException | TimeAfterException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/creditPayment")
    public void payOrderFromCredit(@RequestBody CreditPaymentDto creditPaymentDto) {
        creditPaymentDto.setPaymentType(PaymentType.CREDIT);
        try {
            Optional<Orders> orderById = Optional.ofNullable(orderService.findOrderById(creditPaymentDto.getOrderId()).orElseThrow(() -> new OrderExistenceException("order not found!")));
            customerService.payOrderFromCredit(creditPaymentDto, orderById);
        } catch (OrderExistenceException | CustomerNotFoundException | CreditNotEnoughException e) {
            System.out.println(e.getMessage());
        }
    }

//    @GetMapping("/test")
//    public UserDto getCustomer(){
//        Customer customer = new Customer("f", "f", "zed", "ewa");
//
//        List<OfferDto> offerDto = new ArrayList<>();
//        offerDto.add(new OfferDto("aze"));
//        offerDto.add(new OfferDto("a"));
//        offerDto.add(new OfferDto("a"));
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
