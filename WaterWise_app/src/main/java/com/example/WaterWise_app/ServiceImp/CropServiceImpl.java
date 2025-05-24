package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.CropDto;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Mapper.CropMapper;
import com.example.WaterWise_app.Repository.CropRepository;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.ServiceInterface.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CropServiceImpl implements CropService {
    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private FieldRepository fieldRepository;

    public void setCropRepository(CropRepository cropRepository) {
    this.cropRepository = cropRepository;
}

    public void setFieldRepository(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }


    @Override
    public CropDto createCrop(CropDto cropDto) {
        FieldEntity field = fieldRepository.findById(cropDto.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with ID: " + cropDto.getFieldId()));

        CropEntity crop = CropMapper.toEntity(cropDto, field);
        CropEntity savedCrop = cropRepository.save(crop);

        return CropMapper.toDto(savedCrop);
    }

    @Override
    public CropDto getCropById(Long id) {
        CropEntity crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + id));
        return CropMapper.toDto(crop);
    }

    @Override
    public List<CropDto> getAllCrops() {
        return cropRepository.findAll()
                .stream()
                .map(CropMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CropDto updateCrop(Long id, CropDto cropDto) {
        CropEntity existing = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + id));

        existing.setCropType(cropDto.getCropType());
        existing.setGrowthStage(cropDto.getGrowthStage());
        existing.setIrrigationType(cropDto.getIrrigationType());
        existing.setWaterFlowRate(cropDto.getWaterFlowRate());

        CropEntity updated = cropRepository.save(existing);
        return CropMapper.toDto(updated);
    }

    @Override
    public void deleteCrop(Long id) {
        cropRepository.deleteById(id);
    }
}
