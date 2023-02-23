package com.example.final_project_faz3.maktab.ir.data.model.entity;

import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertStatus;
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
//@SuperBuilder
@ToString(callSuper = true)
public class Expert extends User {
    @Convert(converter = ScoringExpertConverter.class)
    ExpertScore expertScore;

    @Enumerated(EnumType.STRING)
    ExpertStatus expertStatus;

    @Basic(fetch = FetchType.LAZY)
    @Column(length = 300000)
    @ToString.Exclude
    byte[] personalPhoto;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    List<SubService> subServiceList = new ArrayList<>();

    @OneToMany
    @ToString.Exclude
    List<Comment> commentList = new ArrayList<>();

    double performance;
}
