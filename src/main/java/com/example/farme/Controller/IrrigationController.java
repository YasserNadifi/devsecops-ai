package com.example.farme.Controller;


import com.example.farme.Service.IrrigationService;
import com.example.farme.model.IrrigationScheduleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/irrigation/schedule")
public class IrrigationController {
    @Autowired
    private IrrigationService irrigationService;

    @PostMapping
    public ResponseEntity<IrrigationScheduleResponse> getIrrigationPlan(@RequestBody FieldRequest request) {
        IrrigationScheduleResponse result = irrigationService.(request);
        return ResponseEntity.ok(result);
    }


}
