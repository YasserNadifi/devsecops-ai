package com.example.WaterWise_app;
import com.example.WaterWise_app.Dto.CropDto;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Repository.CropRepository;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.ServiceImp.CropServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CropServiceImplTest {

    private CropRepository cropRepository;
    private FieldRepository fieldRepository;
    private CropServiceImpl cropService;

    @BeforeEach
    void setUp() {
        cropRepository = mock(CropRepository.class);
        fieldRepository = mock(FieldRepository.class);
        cropService = new CropServiceImpl();
        cropService.setCropRepository(cropRepository);   // Utilise setter si @Autowired n’est pas utilisé dans les tests
        cropService.setFieldRepository(fieldRepository);
    }

    @Test
    void createCrop_shouldReturnSavedDto() {
        CropDto dto = new CropDto();
        dto.setCropType("Maïs");
        dto.setGrowthStage("Floraison");
        dto.setIrrigationType("Goutte-à-goutte");
        dto.setWaterFlowRate(5.5);
        dto.setFieldId(1L);

        FieldEntity field = new FieldEntity();
        field.setId(1L);

        CropEntity savedEntity = new CropEntity();
        savedEntity.setId(10L);
        savedEntity.setCropType("Maïs");
        savedEntity.setGrowthStage("Floraison");
        savedEntity.setIrrigationType("Goutte-à-goutte");
        savedEntity.setWaterFlowRate(5.5);
        savedEntity.setField(field);

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(cropRepository.save(any())).thenReturn(savedEntity);

        CropDto result = cropService.createCrop(dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Maïs", result.getCropType());
    }

    @Test
    void createCrop_withInvalidFieldId_shouldThrow() {
        CropDto dto = new CropDto();
        dto.setFieldId(999L);

        when(fieldRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cropService.createCrop(dto);
        });

        assertTrue(exception.getMessage().contains("Field not found"));
    }

    @Test
    void getCropById_shouldReturnDto() {
        CropEntity crop = new CropEntity();
        crop.setId(2L);
        crop.setCropType("Blé");
        crop.setGrowthStage("Récolte");
        crop.setIrrigationType("Aspersion");
        crop.setWaterFlowRate(6.0);

        when(cropRepository.findById(2L)).thenReturn(Optional.of(crop));

        CropDto result = cropService.getCropById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Blé", result.getCropType());
    }

    @Test
    void getCropById_notFound_shouldThrow() {
        when(cropRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cropService.getCropById(123L);
        });

        assertTrue(exception.getMessage().contains("Crop not found"));
    }

    @Test
    void getAllCrops_shouldReturnList() {
        CropEntity crop1 = new CropEntity();
        crop1.setId(1L);
        crop1.setCropType("Blé");

        CropEntity crop2 = new CropEntity();
        crop2.setId(2L);
        crop2.setCropType("Tomate");

        when(cropRepository.findAll()).thenReturn(Arrays.asList(crop1, crop2));

        List<CropDto> result = cropService.getAllCrops();

        assertEquals(2, result.size());
    }

    @Test
    void updateCrop_shouldUpdateAndReturnDto() {
        Long id = 5L;

        CropEntity existing = new CropEntity();
        existing.setId(id);
        existing.setCropType("Ancien");
        existing.setGrowthStage("Ancien");
        existing.setIrrigationType("Ancien");
        existing.setWaterFlowRate(1.0);

        CropDto updateDto = new CropDto();
        updateDto.setCropType("Nouveau");
        updateDto.setGrowthStage("Floraison");
        updateDto.setIrrigationType("Aspersion");
        updateDto.setWaterFlowRate(9.9);

        when(cropRepository.findById(id)).thenReturn(Optional.of(existing));
        when(cropRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CropDto result = cropService.updateCrop(id, updateDto);

        assertEquals("Nouveau", result.getCropType());
        assertEquals("Floraison", result.getGrowthStage());
        assertEquals(9.9, result.getWaterFlowRate());
    }

    @Test
    void updateCrop_notFound_shouldThrow() {
        when(cropRepository.findById(404L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cropService.updateCrop(404L, new CropDto());
        });

        assertTrue(exception.getMessage().contains("Crop not found"));
    }

    @Test
    void deleteCrop_shouldCallRepository() {
        cropService.deleteCrop(33L);
        verify(cropRepository).deleteById(33L);
    }
}