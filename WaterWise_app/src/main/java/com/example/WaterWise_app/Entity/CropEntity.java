package com.example.WaterWise_app.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "crops")
@Data
public class CropEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cropType; // Ex: "Maïs", "Blé", "Tomate"

    @Column(nullable = false)
    private String growthStage; // Ex: "Germination", "Floraison", "Récolte"

    @Column(nullable = false)
    private String irrigationType; // Ex: "Goutte-à-goutte", "Aspersion"

    @Column(nullable = false)
    private double waterFlowRate; // Débit d'eau en gallons/minute (gal/min)

    // Relation One-to-One avec FieldEntity
    @OneToOne
    @JoinColumn(name = "field_id") // Clé étrangère dans la table 'crops'
    private FieldEntity field;

}
