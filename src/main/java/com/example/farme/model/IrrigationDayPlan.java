package com.example.farme.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class IrrigationDayPlan {
    private  LocalDate date ;
    private  double waterAmountGallons;
    private  LocalTime endTime ;

    public IrrigationDayPlan() {
    }

    public IrrigationDayPlan(LocalDate date, double waterAmountGallons, LocalTime endTime) {
        this.date = date;
        this.waterAmountGallons = waterAmountGallons;
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWaterAmountGallons() {
        return waterAmountGallons;
    }

    public void setWaterAmountGallons(double waterAmountGallons) {
        this.waterAmountGallons = waterAmountGallons;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}


