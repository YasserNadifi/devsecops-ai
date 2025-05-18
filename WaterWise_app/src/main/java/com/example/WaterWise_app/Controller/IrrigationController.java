package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.ServiceImp.OpenETService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/irrigation")
public class IrrigationController {

    private final OpenETService openETService;

    @Autowired
    public IrrigationController(OpenETService openETService) {
        this.openETService = openETService;
    }

    /**
     * Exemple d’endpoint POST qui reçoit un CropEntity JSON,
     * calcule les besoins d’eau sur une semaine, et retourne la liste des résultats.
     *
     * @param cropId le Id du crop
     * @return liste des besoins quotidiens en irrigation
     */
    @GetMapping("/weekly-needs/{cropId}")
    public ResponseEntity<List<OpenETService.DailyIrrigation>> getWeeklyIrrigationNeeds(@PathVariable Long cropId) {
        List<OpenETService.DailyIrrigation> irrigationNeeds = openETService.calculateWeeklyWaterNeeds(cropId);
        return ResponseEntity.ok(irrigationNeeds);
    }
}
