package com.example.final_project_faz3.maktab.ir.data.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(unique = true)
    private String userName;
    private String password;

    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
