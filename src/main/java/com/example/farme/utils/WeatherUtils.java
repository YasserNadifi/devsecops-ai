package com.example.farme.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class WeatherUtils {

    public List<Double> getPrecipitationForecast(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("daily", "precipitation_sum")
                .queryParam("timezone", "auto")
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("daily")) return null;

        Map dailyData = (Map) response.get("daily");
        return (List<Double>) dailyData.get("precipitation_sum");
    }
}
