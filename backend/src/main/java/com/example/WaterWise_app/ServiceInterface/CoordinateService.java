package com.example.WaterWise_app.ServiceInterface;

import com.example.WaterWise_app.Dto.CoordinateDTO;

import java.util.List;

public interface CoordinateService {
    CoordinateDTO saveCoordinate(CoordinateDTO coordinateDTO);

    List<CoordinateDTO> getAllCoordinates();

    CoordinateDTO getCoordinateById(Long id);

    void deleteCoordinate(Long id);
}
