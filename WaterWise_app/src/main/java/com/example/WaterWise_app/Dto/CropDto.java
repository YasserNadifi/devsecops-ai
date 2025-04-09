package com.example.WaterWise_app.Dto;

import lombok.Data;

@Data
public class CropDto {
    // Champs pour toutes les opérations (CRUD)
    private Long id; // Null à la création, rempli en réponse


    private String cropType; // Ex: "Maïs", "Blé", "Tomate"


    private String growthStage; // Ex: "Germination", "Floraison"


    private String irrigationType; // Ex: "Goutte-à-goutte"


    private double waterFlowRate; // gallons/minute


    private Long fieldId; // Pour lier à FieldEntity

}
