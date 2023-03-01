package com.example.final_project_faz3.maktab.ir.data.model.entity;

import com.example.final_project_faz3.maktab.ir.data.model.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    Customer customer;

    @Column(nullable = false)
    Long proposedPrice;

    @Column(nullable = false)
    String explanation;


    @Temporal(value = TemporalType.DATE)
    @JsonProperty("callEndTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    @CreationTimestamp
    Date date;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @OneToOne
    SubService subService;

}
