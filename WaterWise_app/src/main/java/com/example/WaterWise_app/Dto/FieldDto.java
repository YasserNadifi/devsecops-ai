package com.example.WaterWise_app.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FieldDto {
    private Long id; // ID du champ

    // Liste des coordonnées géographiques (lat, long)
    private List<CoordinateDTO> coordinates;

    // Optionnel : Id de la culture liée, s'il y en a une
    private Long cropId;
}
