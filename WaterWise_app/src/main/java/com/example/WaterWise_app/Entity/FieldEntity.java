package com.example.WaterWise_app.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FieldEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
