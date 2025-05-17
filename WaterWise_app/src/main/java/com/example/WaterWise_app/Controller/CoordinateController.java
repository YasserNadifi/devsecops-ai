package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Dto.CoordinateDTO;
import com.example.WaterWise_app.ServiceInterface.CoordinateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
public class CoordinateController {

    private final CoordinateService coordinateService;

    public CoordinateController(CoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }

    // Créer ou mettre à jour une coordonnée
    @PostMapping
    public ResponseEntity<CoordinateDTO> createCoordinate(@RequestBody CoordinateDTO coordinateDTO) {
        CoordinateDTO savedCoordinate = coordinateService.saveCoordinate(coordinateDTO);
        return ResponseEntity.ok(savedCoordinate);
    }

    // Récupérer toutes les coordonnées
    @GetMapping
    public ResponseEntity<List<CoordinateDTO>> getAllCoordinates() {
        List<CoordinateDTO> coordinates = coordinateService.getAllCoordinates();
        return ResponseEntity.ok(coordinates);
    }

    // Récupérer une coordonnée par ID
    @GetMapping("/{id}")
    public ResponseEntity<CoordinateDTO> getCoordinateById(@PathVariable Long id) {
        CoordinateDTO coordinate = coordinateService.getCoordinateById(id);
        return ResponseEntity.ok(coordinate);
    }

    // Supprimer une coordonnée par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinate(@PathVariable Long id) {
        coordinateService.deleteCoordinate(id);
        return ResponseEntity.noContent().build();
    }

}
