package com.example.final_project_faz3.maktab.ir.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long orderId;

    private int score;

    private String comment;

    private String customerEmail;
}
