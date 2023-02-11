package com.example.fnal_project_faz3.maktab.ir.entity.enumeration;

import com.example.fnal_project_faz3.maktab.ir.entity.Expert;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.util.Date;

@Entity
@Data
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


}
