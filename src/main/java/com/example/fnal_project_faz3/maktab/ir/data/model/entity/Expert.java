package com.example.fnal_project_faz3.maktab.ir.data.model.entity;

import com.example.fnal_project_faz3.maktab.ir.data.model.enumeration.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Expert extends Users {
    @Enumerated(EnumType.STRING)
    ExpertStatus expertStatus;

    @Basic(fetch = FetchType.LAZY)
    @Column(length = 300000)
    @ToString.Exclude
    private byte[] personalPhoto;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    List<SubService> subServiceList = new ArrayList<>();

    @OneToMany
    @ToString.Exclude
    List<Comment> commentList = new ArrayList<>();

    Long credit;
}
