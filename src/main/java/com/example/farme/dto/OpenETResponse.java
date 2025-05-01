package com.example.farme.dto;
public class OpenETResponse {
    public static class DataPoint {
        private String time;
        private double et;

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public double getEt() { return et; }
        public void setEt(double et) { this.et = et; }
    }
}