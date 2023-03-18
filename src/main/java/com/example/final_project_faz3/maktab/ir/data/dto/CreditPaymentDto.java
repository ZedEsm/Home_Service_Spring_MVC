package com.example.final_project_faz3.maktab.ir.data.dto;

import com.example.final_project_faz3.maktab.ir.data.model.enumeration.PaymentType;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditPaymentDto {
    private String customerEmail;
    private Long orderId;
    private Long offerId;
    private PaymentType paymentType;
}
