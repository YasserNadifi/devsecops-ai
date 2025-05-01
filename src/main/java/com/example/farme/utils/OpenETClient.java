package com.example.farme.utils;


import com.example.farme.dto.OpenETRequestBody;
import com.example.farme.dto.OpenETResponse;
import com.example.farme.model.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenETClient {

    @Value("${openet.api.key}")
    private String apiKey;

    @Value("${openet.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OpenETClient() {
        this.restTemplate = new RestTemplate();
    }

    @Autowired
    public OpenETClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Double> getDailyETData(List<Coordinate> coordinates) {

        if (coordinates == null || coordinates.isEmpty()) {
            System.err.println("❌ Liste des coordonnées vide → OpenET ignoré.");
            return Collections.emptyList(); // évite l'appel avec [0.0, 0.0]
        }

        double avgLat = coordinates.stream().mapToDouble(Coordinate::getLatitude).average().orElse(0.0);
        double avgLon = coordinates.stream().mapToDouble(Coordinate::getLongitude).average().orElse(0.0);

        System.out.println("Coordonnées envoyées à OpenET : Lon = " + avgLon + " / Lat = " + avgLat);

        OpenETRequestBody body = new OpenETRequestBody();
        body.setDate_range(List.of("2025-04-01", "2025-04-08"));
        body.setGeometry(List.of(avgLon, avgLat));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        HttpEntity<OpenETRequestBody> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<List<OpenETResponse.DataPoint>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<OpenETResponse.DataPoint>>() {}
            );


            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody()
                        .stream()
                        .map(OpenETResponse.DataPoint::getEt)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
