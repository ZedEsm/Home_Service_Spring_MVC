package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Customer;
import com.example.final_project_faz3.maktab.ir.data.model.entity.Orders;
import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import com.example.final_project_faz3.maktab.ir.data.repository.OrderRepository;
import com.example.final_project_faz3.maktab.ir.exceptions.OrderExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.OrderRegistrationFailedException;
import com.example.final_project_faz3.maktab.ir.exceptions.UnTimeOrderException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final SubServicesService subServicesService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService, SubServicesService subServicesService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.subServicesService = subServicesService;
    }

    @Transactional

    public void submitOrder(Long customerId, Orders orders, Long subServiceId) throws OrderExistenceException, OrderRegistrationFailedException, UnTimeOrderException {
        Optional<Orders> orderById = findOrderById(orders.getId());
        if (orderById.isPresent()) {
            throw new OrderExistenceException("this order already exist!");
        }
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        orders.setOrderStatus(OrderStatus.WAITING_EXPERT_PROPOSE);
        Optional<Customer> customerById = customerService.findCustomerById(customerId);
        Optional<SubService> subService = subServicesService.findSubServiceById(subServiceId);
        if (customerById.isPresent()) {
            if (subService.isPresent()) {
                orders.setSubService(subService.get());
                customerById.get().getOrdersList().add(orders);
                orders.setCustomer(customerById.get());
                orderRepository.save(orders);
            }
        }
        check(orders, date);

    }

    private void check(Orders orders, Date date) throws OrderRegistrationFailedException, UnTimeOrderException, OrderExistenceException {
        Orders orders1 = orderRepository.findById(orders.getId()).orElseThrow(() -> new OrderExistenceException("order not found"));
        if (orders1.getProposedPrice().compareTo((long) orders1.getSubService().getBasePrice()) < 0) {
            deleteOrder(orders1);
            throw new OrderRegistrationFailedException("price is not enough");
        }
        LocalDate localDate1 = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate = LocalDateTime.ofInstant(orders1.getDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
        if (localDate.compareTo(localDate1) < 0) {
            deleteOrder(orders1);
            throw new UnTimeOrderException("time of order submission not now!");
        }
    }

    public Optional<Orders> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(Orders orders) {
        orderRepository.delete(orders);
    }

}
