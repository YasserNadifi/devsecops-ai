package com.example.farme.Dto;

import java.util.List;

public class OpenETResponse {
    private List<DataPoint> data;

    public static class DataPoint {
        private String time;
        private double et;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getEt() {
            return et;
        }

        public void setEt(double et) {
            this.et = et;
        }
    }

    public List<DataPoint> getData() {
        return data;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
    }
}
