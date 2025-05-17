package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.FieldDto;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Mapper.FieldMapper;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.ServiceInterface.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public FieldDto createField(FieldDto fieldDto) {
        FieldEntity entity = FieldMapper.toEntity(fieldDto);
        FieldEntity saved = fieldRepository.save(entity);
        return FieldMapper.toDto(saved);
    }

    @Override
    public FieldDto getFieldById(Long id) {
        FieldEntity entity = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found with id: " + id));
        return FieldMapper.toDto(entity);
    }

    @Override
    public List<FieldDto> getAllFields() {
        return fieldRepository.findAll()
                .stream()
                .map(FieldMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FieldDto updateField(Long id, FieldDto fieldDto) {
        FieldEntity existing = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found with id: " + id));

        // Met à jour les coordonnées (tu peux gérer ici la logique plus fine si besoin)
        existing.setCoordinates(FieldMapper.toEntity(fieldDto).getCoordinates());

        // Mets à jour d'autres propriétés si tu en ajoutes plus tard

        FieldEntity updated = fieldRepository.save(existing);
        return FieldMapper.toDto(updated);
    }

    @Override
    public void deleteField(Long id) {
        FieldEntity existing = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found with id: " + id));
        fieldRepository.delete(existing);
    }

}
