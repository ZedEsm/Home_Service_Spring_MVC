package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.dto.CommentDto;
import com.example.final_project_faz3.maktab.ir.data.dto.CreditPaymentDto;
import com.example.final_project_faz3.maktab.ir.data.dto.OffersDto;
import com.example.final_project_faz3.maktab.ir.data.dto.OnlinePaymentDto;
import com.example.final_project_faz3.maktab.ir.data.model.entity.*;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.PaymentType;
import com.example.final_project_faz3.maktab.ir.exceptions.*;
import com.example.final_project_faz3.maktab.ir.service.*;
import com.example.final_project_faz3.maktab.ir.util.sort.MySort;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")

public class CustomerController {
    private final ScoringExpertConverter scoringExpertConverter = new ScoringExpertConverter();
    private final CustomerService customerService;
    private final SubServicesService subServicesService;
    private final ServicesService servicesService;
    private final OrderService orderService;
    private final AddressService addressService;
    private final ExpertService expertService;
    private final OfferService offerService;


    @Autowired
    public CustomerController(CustomerService customerService, SubServicesService subServicesService, ServicesService servicesService, OrderService orderService, AddressService addressService, ExpertService expertService, OfferService offerService) {
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

    @CrossOrigin
    @PostMapping("/pay-online")
    public String payOnline(@Valid @ModelAttribute OnlinePaymentDto onlinePaymentDto, HttpServletRequest request) {
        CreditPaymentDto creditPaymentDto = new CreditPaymentDto();
        creditPaymentDto.setPaymentType(PaymentType.ONLINE);
        creditPaymentDto.setOrderId(onlinePaymentDto.getOrderId());
        Optional<Orders> orderById = (orderService.findOrderById(creditPaymentDto.getOrderId()));

        try {
            Date expirationDate = new SimpleDateFormat("yyyy-MM").parse(onlinePaymentDto.getExpirationDate());
            if (expirationDate.before(new Date()))
                throw new CardDateExpiredException("card date expired");
        } catch (ParseException | CardDateExpiredException e) {
            System.out.println(e.getMessage());
        }
        if (!onlinePaymentDto.getCaptcha().equals(request.getSession().getAttribute("captcha")))
            try {
                throw new InvalidCaptchaException("captcha is invalid");
            } catch (InvalidCaptchaException e) {
                System.out.println(e.getMessage());
            }
        try {
            customerService.payOrderFromCredit(creditPaymentDto, orderById);
        } catch (CustomerNotFoundException | OrderExistenceException | CreditNotEnoughException e) {
            System.out.println(e.getMessage());
        }

        return "Order Payed";
    }

    @PostMapping("/addComment")
    public void addComment(@RequestBody CommentDto commentDto) {//TODO:enabled active kon & see comments & add photo to expert handel

        try {
            Optional<Customer> customerByEmail = Optional.ofNullable(customerService.findCustomerByEmail(commentDto.getCustomerEmail()).orElseThrow(() -> new CustomerNotFoundException("customer not found!")));
            Optional<Orders> orderById = Optional.ofNullable(orderService.findOrderById(commentDto.getOrderId()).orElseThrow(() -> new OrderExistenceException("order not found")));
            Expert expert = expertService.findExpertById(commentDto.getExpertId());
            Comment comment = getComment(commentDto, customerByEmail, expert);
            comment.setOrders(orderById.get());
            comment.getOrders().setId(commentDto.getOrderId());
            customerService.addComment(comment, orderById);
        } catch (OrderExistenceException | CustomerNotFoundException | ExpertExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

    private Comment getComment(CommentDto commentDto, Optional<Customer> customerByEmail, Expert expert) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setCustomer(customerByEmail.get());
        comment.setExpert(expert);

        comment.getCustomer().setEmailAddress(commentDto.getCustomerEmail());
        comment.getExpert().setPerformance(commentDto.getScore());
        ExpertScore expertScore = scoringExpertConverter.convertToEntityAttribute(commentDto.getScore());
        comment.getExpert().setExpertScore(expertScore);

        return comment;
    }
}