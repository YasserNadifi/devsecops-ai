package com.example.WaterWise_app.ServiceInterface;

import com.example.WaterWise_app.Dto.CropDto;

import java.util.List;

public interface CropService {

    CropDto createCrop(CropDto cropDto);
    CropDto getCropById(Long id);
    List<CropDto> getAllCrops();
    CropDto updateCrop(Long id, CropDto cropDto);
    void deleteCrop(Long id);
}
