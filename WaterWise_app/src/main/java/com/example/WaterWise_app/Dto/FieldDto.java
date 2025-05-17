package com.example.WaterWise_app.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FieldDto {
    private Long id; // ID du champ

    private String name;

    // Liste des coordonnées géographiques (lat, long)
    private List<CoordinateDTO> coordinates;

    // Optionnel : Id de la culture liée, s'il y en a une
    private Long cropId;

    // Id de l'utilisateur propriétaire du champ
    private Long userId;
}
