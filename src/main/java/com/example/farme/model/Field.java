package com.example.farme.model;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String name;

    @ElementCollection
    private List<Coordinate> coordinates;
    private  String cropType;
    private String growthStage;
    private String irrigationType;
    private double flowRate;

    // Getters and Setters
}
