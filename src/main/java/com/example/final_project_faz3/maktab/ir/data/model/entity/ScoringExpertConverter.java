package com.example.final_project_faz3.maktab.ir.data.model.entity;

import com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static com.example.final_project_faz3.maktab.ir.data.model.enumeration.ExpertScore.*;


@Converter
public class ScoringExpertConverter implements AttributeConverter<ExpertScore, Integer> {


    @Override
    public Integer convertToDatabaseColumn(ExpertScore attribute) {
        if (attribute == null)
            return null;

        return switch (attribute) {
            case UNCOMMENT_YET -> 0;
            case BAD -> 1;
            case POOR -> 2;
            case FAIR -> 3;
            case GOOD -> 4;
            case EXCELLENT -> 5;
            default -> throw new IllegalArgumentException(attribute + " not supported.");
        };
    }

    @Override
    public ExpertScore convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        return switch (dbData) {
            case 0 -> UNCOMMENT_YET;
            case 1 -> BAD;
            case 2 -> POOR;
            case 3 -> FAIR;
            case 4 -> GOOD;
            case 5 -> EXCELLENT;
            default -> throw new IllegalArgumentException(dbData + " not supported.");
        };
    }
}




