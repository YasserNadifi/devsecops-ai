package com.example.farme.Service;

import com.example.farme.model.IrrigationScheduleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.farme.dto.FieldRequest;
import org.springframework.stereotype.Service;

@Service
public class IrrigationServiceImpl implements IrrigationService {

    @Autowired
    private FieldService fieldService;

    @Override
    public IrrigationScheduleResponse generateIrrigationSchedule(FieldRequest request) {
        return fieldService.generateIrrigationSchedule(request.getFieldId());
    }
}
