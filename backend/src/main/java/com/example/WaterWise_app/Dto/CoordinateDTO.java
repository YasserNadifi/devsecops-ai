package com.example.WaterWise_app.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class CoordinateDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private Long fieldId;
}
