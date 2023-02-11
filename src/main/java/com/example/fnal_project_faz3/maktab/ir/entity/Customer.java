package com.example.fnal_project_faz3.maktab.ir.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Customer extends Users {


}
