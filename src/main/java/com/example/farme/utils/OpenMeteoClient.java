package com.example.farme.utils;

import com.example.farme.dto.OpenMeteoResponse;
import com.example.farme.model.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
public class OpenMeteoClient {

    @Autowired
    private RestTemplate restTemplate;

    public List<Double> getDailyPrecipitationData(List<Coordinate> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            System.err.println("Coordonnées vides appel Open-Meteo ignoré.");
            return Collections.emptyList();
        }

        double avgLat = coordinates.stream().mapToDouble(Coordinate::getLatitude).average().orElse(0.0);
        double avgLon = coordinates.stream().mapToDouble(Coordinate::getLongitude).average().orElse(0.0);

        System.out.println("Coordonnées envoyées a Open-Meteo : Lon = " + avgLon + " / Lat = " + avgLat);

        String url = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", avgLat)
                .queryParam("longitude", avgLon)
                .queryParam("daily", "precipitation_sum")
                .queryParam("start_date", "2025-05-01")
                .queryParam("end_date", "2025-05-08")
                .queryParam("timezone", "auto")
                .toUriString();

        try {
            OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

            if (response != null && response.getDaily() != null) {
                return response.getDaily().getPrecipitation_sum();
            } else {
                System.err.println("⚠️ Réponse vide d'Open-Meteo");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à Open-Meteo : " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
