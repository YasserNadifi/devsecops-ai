package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.CoordinateDTO;
import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Mapper.CoordinateMapper;
import com.example.WaterWise_app.Repository.CoordinateRepository;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.ServiceInterface.CoordinateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoordinateServiceImpl implements CoordinateService {

    private final CoordinateRepository coordinateRepository;
    private final FieldRepository fieldRepository; // pour récupérer FieldEntity depuis fieldId

    public CoordinateServiceImpl(CoordinateRepository coordinateRepository, FieldRepository fieldRepository) {
        this.coordinateRepository = coordinateRepository;
        this.fieldRepository = fieldRepository;
    }

    @Override
    public CoordinateDTO saveCoordinate(CoordinateDTO dto) {
        FieldEntity field = null;
        if (dto.getFieldId() != null) {
            Optional<FieldEntity> fieldOpt = fieldRepository.findById(dto.getFieldId());
            if (fieldOpt.isPresent()) {
                field = fieldOpt.get();
            } else {
                throw new RuntimeException("Field non trouvé avec id: " + dto.getFieldId());
            }
        }
        CoordinateEntity entity = CoordinateMapper.toEntity(dto, field);
        CoordinateEntity saved = coordinateRepository.save(entity);
        return CoordinateMapper.toDTO(saved);
    }

    @Override
    public List<CoordinateDTO> getAllCoordinates() {
        List<CoordinateEntity> entities = coordinateRepository.findAll();
        return entities.stream()
                .map(CoordinateMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CoordinateDTO getCoordinateById(Long id) {
        CoordinateEntity entity = coordinateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordinate non trouvé avec id: " + id));
        return CoordinateMapper.toDTO(entity);
    }

    @Override
    public void deleteCoordinate(Long id) {
        coordinateRepository.deleteById(id);
    }
}
