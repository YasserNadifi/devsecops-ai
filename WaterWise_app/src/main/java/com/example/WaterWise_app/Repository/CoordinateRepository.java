package com.example.WaterWise_app.Repository;

import com.example.WaterWise_app.Entity.CoordinateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends JpaRepository<CoordinateEntity, Long> {
    // Tu peux ajouter des méthodes personnalisées si besoin
}
