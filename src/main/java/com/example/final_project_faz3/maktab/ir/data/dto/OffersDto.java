package com.example.final_project_faz3.maktab.ir.data.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OffersDto {

    private Date date;

    private Long proposedPrice;

    private Date timeToStartWork;

    private Long expertId;

    private String expertName;

    //Expert expert;
}
