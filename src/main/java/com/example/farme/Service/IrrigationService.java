package com.example.farme.Service;

import com.example.farme.model.IrrigationScheduleResponse;
import com.example.farme.dto.FieldRequest;

public interface IrrigationService {
    IrrigationScheduleResponse generateIrrigationSchedule(FieldRequest request);

}
