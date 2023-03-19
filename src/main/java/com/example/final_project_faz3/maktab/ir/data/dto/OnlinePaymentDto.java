package com.example.final_project_faz3.maktab.ir.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlinePaymentDto {

    private Long orderId;

    @Pattern(regexp = "^\\d{16}$")
    private String cardNumber;

    @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])$")
    private String expirationDate;

    @Pattern(regexp = "^\\d{3,4}$")
    private String cvv2;

    private String captcha;
}