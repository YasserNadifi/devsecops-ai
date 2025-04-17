package com.example.farme.Controller;

import com.example.farme.model.Field;
import com.example.farme.Service.FieldServiceImpl;
import com.example.farme.model.IrrigationScheduleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fields")
public class FieldController {

    @Autowired
    private FieldServiceImpl fieldService;

    @PostMapping
    public ResponseEntity<Field> saveField(@RequestBody Field field){
        Field savedField = fieldService.saveField(field);
        return ResponseEntity.ok(savedField);
     }

     @GetMapping("/{id}")
    public ResponseEntity<Field> getFieldById(@PathVariable Long id){
        Field field = fieldService.getFieldById(id);
        return ResponseEntity.ok(field);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Field> deleteField(@PathVariable Long id){
        Field field = fieldService.deleteField(id);
        return null;
    }


    @GetMapping("/{id}/irrigation-schedule")
    public ResponseEntity<IrrigationScheduleResponse> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(fieldService.generateIrrigationSchedule(id));
    }



}