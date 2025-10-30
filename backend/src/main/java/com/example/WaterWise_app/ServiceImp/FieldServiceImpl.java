package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.FieldDto;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Entity.User;
import com.example.WaterWise_app.Mapper.FieldMapper;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.Repository.UserRepository;
import com.example.WaterWise_app.ServiceInterface.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public FieldDto createField(FieldDto fieldDto) {
        // Récupérer l'utilisateur lié au champ
        User user = userRepository.findById(fieldDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + fieldDto.getUserId()));

        FieldEntity entity = FieldMapper.toEntity(fieldDto, user);
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

        User user = userRepository.findById(fieldDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + fieldDto.getUserId()));

        // Construit une entité temporaire avec le nouveau DTO + User, pour récupérer coordonnées à setter
        FieldEntity updatedEntity = FieldMapper.toEntity(fieldDto, user);

        // Mise à jour des coordonnées (et potentiellement autres champs si ajoutés)
        existing.setCoordinates(updatedEntity.getCoordinates());

        // Si tu as d'autres champs dans FieldEntity, mets-les à jour ici

        FieldEntity updated = fieldRepository.save(existing);
        return FieldMapper.toDto(updated);
    }

    @Override
    public void deleteField(Long id) {
        FieldEntity existing = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found with id: " + id));
        fieldRepository.delete(existing);
    }

    @Override
    public List<FieldDto> getFieldsByUserId(Long userId) {
        List<FieldEntity> fields = fieldRepository.findByUserId(userId);
        return fields.stream()
                .map(FieldMapper::toDto)
                .collect(Collectors.toList());
    }
}
