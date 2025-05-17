package com.example.WaterWise_app.Mapper;

import com.example.WaterWise_app.Dto.CoordinateDTO;
import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.Entity.FieldEntity;

public class CoordinateMapper {

    public static CoordinateDTO toDTO(CoordinateEntity entity) {
        CoordinateDTO dto = new CoordinateDTO();
        dto.setId(entity.getId());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setFieldId(entity.getField() != null ? entity.getField().getId() : null);
        return dto;
    }

    public static CoordinateEntity toEntity(CoordinateDTO dto, FieldEntity field) {
        CoordinateEntity entity = new CoordinateEntity();
        entity.setId(dto.getId());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setField(field);
        return entity;
    }
}
