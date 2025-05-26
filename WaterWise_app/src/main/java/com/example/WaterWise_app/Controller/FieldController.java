package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Dto.FieldDto;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Repository.CropRepository;
import com.example.WaterWise_app.ServiceImp.OpenETService;
import com.example.WaterWise_app.ServiceInterface.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:30573"})
@RestController
@RequestMapping("/api/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private OpenETService openETService;

    @Autowired
    private CropRepository cropRepository;

    @PostMapping
    public ResponseEntity<FieldDto> createField(@RequestBody FieldDto fieldDto) {
        FieldDto created = fieldService.createField(fieldDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldDto> getFieldById(@PathVariable Long id) {
        FieldDto fieldDto = fieldService.getFieldById(id);
        return ResponseEntity.ok(fieldDto);
    }

    @GetMapping
    public ResponseEntity<List<FieldDto>> getAllFields() {
        List<FieldDto> fields = fieldService.getAllFields();
        return ResponseEntity.ok(fields);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldDto> updateField(@PathVariable Long id, @RequestBody FieldDto fieldDto) {
        FieldDto updated = fieldService.updateField(id, fieldDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FieldDto>> getFieldsByUserId(@PathVariable Long userId) {
        List<FieldDto> fields = fieldService.getFieldsByUserId(userId);
        return ResponseEntity.ok(fields);
    }

    @GetMapping("/surface/{cropId}")
    public double getFieldSurface(@PathVariable Long cropId) {
        CropEntity crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found with id: " + cropId));
        FieldEntity field = crop.getField();
        return openETService.getSurfaceM2(field);
    }

}
