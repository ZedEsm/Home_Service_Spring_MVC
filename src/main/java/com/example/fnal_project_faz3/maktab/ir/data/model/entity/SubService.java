package com.example.fnal_project_faz3.maktab.ir.data.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuperBuilder
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    Services service;
    private int basePrice;
    private String description;

    @Column(nullable = false)
    private String name;

    @Override
    public String toString() {
        return "SubService{" +
                "id=" + id +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        SubService subService = (SubService) o;
//        return subService.description.equals(this.description) && subService.basePrice == this.basePrice;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubService that = (SubService) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}