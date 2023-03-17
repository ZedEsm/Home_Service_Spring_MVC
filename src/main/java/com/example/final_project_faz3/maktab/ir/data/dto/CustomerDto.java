package com.example.final_project_faz3.maktab.ir.data.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CustomerDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^([\\da-zA-Z]){8,}$")
    private String password;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z ]{2,}")
    private String lastName;
}
