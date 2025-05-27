package com.example.WaterWise_app;

import com.example.WaterWise_app.Dto.CoordinateDTO;
import com.example.WaterWise_app.Dto.FieldDto;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Entity.User;
import com.example.WaterWise_app.Mapper.FieldMapper;
import com.example.WaterWise_app.Repository.FieldRepository;
import com.example.WaterWise_app.Repository.UserRepository;
import com.example.WaterWise_app.ServiceImp.FieldServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FieldServiceImplTest {

    @InjectMocks
    private FieldServiceImpl fieldService;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private UserRepository userRepository;

    private User mockUser;
    private FieldEntity mockFieldEntity;
    private FieldDto mockFieldDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User
        mockUser = new User();
        mockUser.setId(1L);

        // Mock FieldEntity
        mockFieldEntity = new FieldEntity();
        mockFieldEntity.setId(10L);
        mockFieldEntity.setName("Field 1");
        mockFieldEntity.setUser(mockUser);
        mockFieldEntity.setCoordinates(Collections.emptyList());

        // Mock FieldDto
        mockFieldDto = new FieldDto();
        mockFieldDto.setId(10L);
        mockFieldDto.setName("Field 1");
        mockFieldDto.setUserId(1L);
        mockFieldDto.setCoordinates(Collections.emptyList());
    }

    @Test
    void createField_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(fieldRepository.save(any(FieldEntity.class))).thenReturn(mockFieldEntity);

        FieldDto created = fieldService.createField(mockFieldDto);

        assertNotNull(created);
        assertEquals(mockFieldDto.getName(), created.getName());
        assertEquals(mockFieldDto.getUserId(), created.getUserId());

        verify(userRepository, times(1)).findById(1L);
        verify(fieldRepository, times(1)).save(any(FieldEntity.class));
    }

    @Test
    void createField_userNotFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fieldService.createField(mockFieldDto);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(fieldRepository, never()).save(any());
    }

    @Test
    void getFieldById_success() {
        when(fieldRepository.findById(10L)).thenReturn(Optional.of(mockFieldEntity));

        FieldDto dto = fieldService.getFieldById(10L);

        assertNotNull(dto);
        assertEquals("Field 1", dto.getName());

        verify(fieldRepository, times(1)).findById(10L);
    }

    @Test
    void getFieldById_notFound_shouldThrow() {
        when(fieldRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fieldService.getFieldById(10L);
        });

        assertTrue(exception.getMessage().contains("Field not found"));
    }

    @Test
    void getAllFields_success() {
        List<FieldEntity> list = List.of(mockFieldEntity);
        when(fieldRepository.findAll()).thenReturn(list);

        List<FieldDto> all = fieldService.getAllFields();

        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals("Field 1", all.get(0).getName());
    }

    @Test
    void updateField_success() {
        FieldDto updateDto = new FieldDto();
        updateDto.setId(10L);
        updateDto.setName("Updated Field");
        updateDto.setUserId(1L);
        updateDto.setCoordinates(Collections.emptyList());

        when(fieldRepository.findById(10L)).thenReturn(Optional.of(mockFieldEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(fieldRepository.save(any(FieldEntity.class))).thenReturn(mockFieldEntity);

        FieldDto updated = fieldService.updateField(10L, updateDto);

        assertNotNull(updated);
        // Name is not updated because updateField only updates coordinates in impl
        // You can adjust test depending on your update logic
        assertEquals("Field 1", updated.getName());

        verify(fieldRepository, times(1)).save(any(FieldEntity.class));
    }

    @Test
    void deleteField_success() {
        when(fieldRepository.findById(10L)).thenReturn(Optional.of(mockFieldEntity));

        assertDoesNotThrow(() -> fieldService.deleteField(10L));

        verify(fieldRepository, times(1)).delete(mockFieldEntity);
    }

    @Test
    void getFieldsByUserId_success() {
        when(fieldRepository.findByUserId(1L)).thenReturn(List.of(mockFieldEntity));

        List<FieldDto> result = fieldService.getFieldsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Field 1", result.get(0).getName());
    }
}