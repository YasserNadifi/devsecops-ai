package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Dto.CropDto;
import com.example.WaterWise_app.ServiceInterface.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:30573"})
@RestController
@RequestMapping("/api/crops")
public class CropController {

    @Autowired
    private CropService cropService;

    // ➕ Créer une culture
    @PostMapping
    public CropDto createCrop(@RequestBody CropDto cropDto) {
        return cropService.createCrop(cropDto);
    }

    // 📄 Obtenir une culture par ID
    @GetMapping("/{id}")
    public CropDto getCropById(@PathVariable Long id) {
        return cropService.getCropById(id);
    }

    // 📋 Obtenir toutes les cultures
    @GetMapping
    public List<CropDto> getAllCrops() {
        return cropService.getAllCrops();
    }

    // ✏️ Mettre à jour une culture
    @PutMapping("/{id}")
    public CropDto updateCrop(@PathVariable Long id, @RequestBody CropDto cropDto) {
        return cropService.updateCrop(id, cropDto);
    }

    // ❌ Supprimer une culture
    @DeleteMapping("/{id}")
    public void deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
    }
}
