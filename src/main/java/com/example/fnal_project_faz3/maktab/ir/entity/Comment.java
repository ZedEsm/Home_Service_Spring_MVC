package com.example.fnal_project_faz3.maktab.ir.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.util.Date;
import java.util.List;

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


    @Temporal(value = TemporalType.DATE)
    @CreationTimestamp
    private Date date;

    @Column(nullable = false)
    private Long proposedPrice;


    @Temporal(TemporalType.TIMESTAMP)
    private Date timeToStartWork;


    private Duration durationOfWork;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Expert expert;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Orders order;

    @ElementCollection
    @Column(nullable = false)
    private List<String> comment;

    @ManyToOne
    @ToString.Exclude
    private Customer customer;


}
