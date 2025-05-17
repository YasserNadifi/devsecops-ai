package com.example.WaterWise_app.Repository;

import com.example.WaterWise_app.Entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Long>  {
    List<FieldEntity> findByUserId(Long userId);
}
