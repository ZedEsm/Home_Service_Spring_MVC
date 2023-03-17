package com.example.final_project_faz3.maktab.ir.util.validation.sort;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Offers;

import java.util.Comparator;

public class MySort implements Comparator<Offers> {

    @Override
    public int compare(Offers o1, Offers o2) {
        return (int) (o1.getProposedPrice()-o2.getProposedPrice());
    }
}
