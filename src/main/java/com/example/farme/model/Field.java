package com.example.farme.model;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_seq")
    @SequenceGenerator(name = "field_seq", sequenceName = "field_sequence", allocationSize = 1)
    private  Long id;
    private  String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "coordinate",
            joinColumns = @JoinColumn(name = "field_id")
    )
    private List<Coordinate> coordinates;
    private  String cropType;
    private String growthStage;
    private String irrigationType;
    private double flowRate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public String getCropType() {
        return cropType;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public String getIrrigationType() {
        return irrigationType;
    }

    public void setIrrigationType(String irrigationType) {
        this.irrigationType = irrigationType;
    }

    public double getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(double flowRate) {
        this.flowRate = flowRate;
    }
}
