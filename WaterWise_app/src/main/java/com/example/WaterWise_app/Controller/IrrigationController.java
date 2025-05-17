package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.ServiceImp.OpenETService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/irrigation")
public class IrrigationController {

    private final OpenETService openETService;

    public IrrigationController(OpenETService openETService) {
        this.openETService = openETService;
    }

    @PostMapping("/weekly-needs")
    public List<OpenETService.DailyIrrigation> getWeeklyWaterNeeds(@RequestBody CropEntity crop) {
        return openETService.calculateWeeklyWaterNeeds(crop);
    }
}
