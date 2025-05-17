package com.example.farme.dto;

import java.util.List;

public class OpenMeteoResponse {
    private Daily daily;

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public static class Daily {
        private List<String> time;
        private List<Double> precipitation_sum;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Double> getPrecipitation_sum() {
            return precipitation_sum;
        }

        public void setPrecipitation_sum(List<Double> precipitation_sum) {
            this.precipitation_sum = precipitation_sum;
        }
    }
}
