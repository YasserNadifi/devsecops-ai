package com.example.farme.Service;

import com.example.farme.model.IrrigationScheduleResponse;

public interface IrrigationService {
    IrrigationScheduleResponse generateIrrigationSchedule(FieldRequest request);

}
