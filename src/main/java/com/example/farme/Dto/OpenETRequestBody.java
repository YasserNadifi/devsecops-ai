package com.example.farme.Dto;

import java.util.List;

public class OpenETRequestBody {
    private List<String> date_range;
    private String file_format = "JSON";
    private List<Double> geometry;
    private String interval = "daily";
    private String model = "Ensemble";
    private String reference_et = "gridMET";
    private String units = "mm";
    private String variable = "ET";

    public List<String> getDate_range() {
        return date_range;
    }

    public void setDate_range(List<String> date_range) {
        this.date_range = date_range;
    }

    public String getFile_format() {
        return file_format;
    }

    public void setFile_format(String file_format) {
        this.file_format = file_format;
    }

    public List<Double> getGeometry() {
        return geometry;
    }

    public void setGeometry(List<Double> geometry) {
        this.geometry = geometry;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getReference_et() {
        return reference_et;
    }

    public void setReference_et(String reference_et) {
        this.reference_et = reference_et;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}