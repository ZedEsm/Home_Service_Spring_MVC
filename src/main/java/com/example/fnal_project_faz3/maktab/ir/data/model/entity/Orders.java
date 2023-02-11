package com.example.fnal_project_faz3.maktab.ir.data.model.entity;

import com.example.fnal_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
//@Builder
@Table(name = "customer_order")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @OneToMany
    List<Offers> offersList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    Address address;

    @ManyToOne
    @JoinColumn(nullable = false)
    Customer customer;

    @Column(nullable = false)
    Long proposedPrice;

    @Column(nullable = false)
    String explanation;


    @Temporal(value = TemporalType.DATE)
    Date date;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @OneToOne
    SubService subService;

}
