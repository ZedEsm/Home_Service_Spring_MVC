package com.example.fnal_project_faz3.maktab.ir.data.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Builder
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<SubService> subServiceList;

}
