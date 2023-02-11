package com.example.fnal_project_faz3.maktab.ir.data.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@SuperBuilder
public class Customer extends Users {
    @OneToMany
    List<Orders> ordersList = new ArrayList<>();
}
