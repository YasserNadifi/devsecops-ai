package com.example.WaterWise_app.Mapper;

import com.example.WaterWise_app.Dto.CropDto;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;

public class CropMapper {

    public static CropDto toDto(CropEntity entity) {
        if (entity == null) {
            return null;
        }

        CropDto dto = new CropDto();
        dto.setId(entity.getId());
        dto.setCropType(entity.getCropType());
        dto.setGrowthStage(entity.getGrowthStage());
        dto.setIrrigationType(entity.getIrrigationType());
        dto.setWaterFlowRate(entity.getWaterFlowRate());

        if (entity.getField() != null) {
            dto.setFieldId(entity.getField().getId());
        }

        return dto;
    }

    public static CropEntity toEntity(CropDto dto, FieldEntity fieldEntity) {
        if (dto == null) {
            return null;
        }

        CropEntity entity = new CropEntity();
        entity.setId(dto.getId());
        entity.setCropType(dto.getCropType());
        entity.setGrowthStage(dto.getGrowthStage());
        entity.setIrrigationType(dto.getIrrigationType());
        entity.setWaterFlowRate(dto.getWaterFlowRate());
        entity.setField(fieldEntity); // fieldEntity doit être récupéré avant (ex: depuis le service)

        return entity;
    }
}
