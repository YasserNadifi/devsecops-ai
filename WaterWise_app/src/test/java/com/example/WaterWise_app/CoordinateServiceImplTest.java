package com.example.WaterWise_app;

import com.example.WaterWise_app.Dto.CoordinateDTO;
import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Repository.CoordinateRepository;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.ServiceImp.CoordinateServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoordinateServiceImplTest {

    private CoordinateRepository coordinateRepository;
    private FieldRepository fieldRepository;
    private CoordinateServiceImpl coordinateService;

    @BeforeEach
    void setUp() {
        coordinateRepository = mock(CoordinateRepository.class);
        fieldRepository = mock(FieldRepository.class);
        coordinateService = new CoordinateServiceImpl(coordinateRepository, fieldRepository);
    }

    @Test
    void saveCoordinate_withNullFieldId_shouldSave() {
        CoordinateDTO dto = new CoordinateDTO();
        dto.setLatitude(12.34);
        dto.setLongitude(56.78);
        dto.setFieldId(null);

        CoordinateEntity savedEntity = new CoordinateEntity();
        savedEntity.setId(1L);
        savedEntity.setLatitude(12.34);
        savedEntity.setLongitude(56.78);

        when(coordinateRepository.save(any())).thenReturn(savedEntity);

        CoordinateDTO result = coordinateService.saveCoordinate(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(coordinateRepository).save(any());
    }

    @Test
    void saveCoordinate_withInvalidFieldId_shouldThrow() {
        CoordinateDTO dto = new CoordinateDTO();
        dto.setLatitude(10.0);
        dto.setLongitude(20.0);
        dto.setFieldId(99L);

        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            coordinateService.saveCoordinate(dto);
        });

        assertTrue(exception.getMessage().contains("Field non trouvé avec id"));
    }

    @Test
    void getCoordinateById_withInvalidId_shouldThrow() {
        when(coordinateRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            coordinateService.getCoordinateById(100L);
        });

        assertTrue(exception.getMessage().contains("Coordinate non trouvé avec id"));
    }

    @Test
    void getAllCoordinates_shouldReturnList() {
        CoordinateEntity entity1 = new CoordinateEntity();
        entity1.setId(1L);
        entity1.setLatitude(1.1);
        entity1.setLongitude(2.2);

        CoordinateEntity entity2 = new CoordinateEntity();
        entity2.setId(2L);
        entity2.setLatitude(3.3);
        entity2.setLongitude(4.4);

        when(coordinateRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        List<CoordinateDTO> result = coordinateService.getAllCoordinates();

        assertEquals(2, result.size());
    }

    @Test
    void deleteCoordinate_shouldCallDeleteById() {
        coordinateService.deleteCoordinate(42L);
        verify(coordinateRepository).deleteById(42L);
    }
}