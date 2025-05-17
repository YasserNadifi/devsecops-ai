package com.example.farme.model;

import java.util.List;

public class IrrigationScheduleResponse {
    private  Long fieldId;
    private  String fieldName;
    private List<IrrigationDayPlan> schedule;

    public IrrigationScheduleResponse() {
    }

    public IrrigationScheduleResponse(Long fieldId, String fieldName, List<IrrigationDayPlan> schedule) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.schedule = schedule;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<IrrigationDayPlan> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<IrrigationDayPlan> schedule) {
        this.schedule = schedule;
    }
}

