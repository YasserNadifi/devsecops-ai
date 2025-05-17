package com.example.WaterWise_app.Repository;

import com.example.WaterWise_app.Entity.CropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CropRepository extends JpaRepository<CropEntity, Long> {
    CropEntity findByFieldId(Long fieldId); // Pour récupérer un crop lié à un field
}
