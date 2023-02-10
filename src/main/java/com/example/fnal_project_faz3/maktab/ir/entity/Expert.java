package com.example.fnal_project_faz3.maktab.ir.entity;

import com.example.fnal_project_faz3.maktab.ir.entity.enumeration.ExpertScore;
import com.example.fnal_project_faz3.maktab.ir.entity.enumeration.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends Users {
    @Convert(converter = ScoringExpertConverter.class)
    ExpertScore expertScore;

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
    List<Comment> commentList;

    private Long credit;

    public Expert(String firstName, String lastName, String emailAddress, String password) {
        super(firstName, lastName, emailAddress, password);
    }
}
