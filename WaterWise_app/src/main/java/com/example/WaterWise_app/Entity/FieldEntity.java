package com.example.WaterWise_app.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class FieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "field", cascade = CascadeType.ALL)
    private CropEntity crop;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoordinateEntity> coordinates;

    @ManyToOne(fetch = FetchType.LAZY)  // Un champ appartient à un utilisateur
    @JoinColumn(name = "user_id")       // colonne dans la table FieldEntity
    private User user;
}
