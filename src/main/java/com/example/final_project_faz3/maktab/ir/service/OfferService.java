package com.example.final_project_faz3.maktab.ir.service;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Offers;
import com.example.final_project_faz3.maktab.ir.data.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public void saveOffer(Offers offers) {
        offerRepository.save(offers);
    }

    public Optional<Offers> findOfferById(Long id) {
        return offerRepository.findById(id);
    }
}
