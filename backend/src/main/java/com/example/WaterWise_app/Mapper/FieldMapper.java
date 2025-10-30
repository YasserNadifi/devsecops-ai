package com.example.WaterWise_app.Mapper;

import com.example.WaterWise_app.Dto.CoordinateDTO;
import com.example.WaterWise_app.Dto.FieldDto;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class FieldMapper {

    public static FieldDto toDto(FieldEntity entity) {
        if (entity == null) return null;

        FieldDto dto = new FieldDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        // Utilisation de CoordinateMapper pour convertir la liste des coordonnées
        List<CoordinateDTO> coordinateDtos = entity.getCoordinates()
                .stream()
                .map(coordinate -> CoordinateMapper.toDTO(coordinate))
                .collect(Collectors.toList());
        dto.setCoordinates(coordinateDtos);

        CropEntity crop = entity.getCrop();
        if (crop != null) {
            dto.setCropId(crop.getId());
        }
        User user = entity.getUser();
        if (user != null) {
            dto.setUserId(user.getId());  // Ajout userId dans le DTO
        }

        return dto;
    }

    // Ici on passe l'utilisateur en paramètre car on doit lui assigner dans FieldEntity
    public static FieldEntity toEntity(FieldDto dto, User user) {
        if (dto == null) return null;

        FieldEntity entity = new FieldEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        // Conversion coordonnées
        List<com.example.WaterWise_app.Entity.CoordinateEntity> coordinateEntities = dto.getCoordinates()
                .stream()
                .map(coordDto -> CoordinateMapper.toEntity(coordDto, entity))
                .collect(Collectors.toList());
        entity.setCoordinates(coordinateEntities);

        // Assignation de l'utilisateur
        entity.setUser(user);

        return entity;
    }
}
