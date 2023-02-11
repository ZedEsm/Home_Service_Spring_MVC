package com.example.fnal_project_faz3.maktab.ir.data.model.entity;

import com.example.fnal_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Convert(converter = ScoringExpertConverter.class)
    private ExpertScore expertScore;

}
