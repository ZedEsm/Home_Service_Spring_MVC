package com.example.final_project_faz3.maktab.ir.data.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
//@SuperBuilder
public class Customer extends User {
    @OneToMany
    List<Orders> ordersList = new ArrayList<>();
}