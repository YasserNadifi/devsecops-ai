package com.example.WaterWise_app.ServiceInterface;

import com.example.WaterWise_app.Dto.FieldDto;

import java.util.List;

public interface FieldService {
    FieldDto createField(FieldDto fieldDto);
    FieldDto getFieldById(Long id);
    List<FieldDto> getAllFields();
    FieldDto updateField(Long id, FieldDto fieldDto);
    void deleteField(Long id);
    // Nouvelle méthode pour récupérer tous les champs d'un utilisateur
    List<FieldDto> getFieldsByUserId(Long userId);
}
