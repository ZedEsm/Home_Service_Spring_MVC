package com.example.fnal_project_faz3.maktab.ir.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offers {
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

    @OneToOne
    Expert expert;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Offers offers = (Offers) o;
        return id != null && Objects.equals(id, offers.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
