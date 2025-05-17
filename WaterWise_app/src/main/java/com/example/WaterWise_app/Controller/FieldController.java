package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Dto.FieldDto;
import com.example.WaterWise_app.ServiceInterface.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

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
}
