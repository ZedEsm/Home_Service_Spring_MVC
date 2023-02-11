package com.example.fnal_project_faz3.maktab.ir.entity;

import com.example.fnal_project_faz3.maktab.ir.entity.enumeration.ExpertScore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
